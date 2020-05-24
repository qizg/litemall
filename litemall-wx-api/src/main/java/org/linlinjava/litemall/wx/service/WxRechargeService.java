package org.linlinjava.litemall.wx.service;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.notify.NotifyService;
import org.linlinjava.litemall.core.util.DateTimeUtil;
import org.linlinjava.litemall.core.util.IpUtil;
import org.linlinjava.litemall.core.util.JacksonUtil;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallRecharge;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallRechargeService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.linlinjava.litemall.wx.util.WxResponseCode.AUTH_OPENID_UNACCESS;
import static org.linlinjava.litemall.wx.util.WxResponseCode.ORDER_PAY_FAIL;

@Service
public class WxRechargeService {

    private final Log logger = LogFactory.getLog(WxRechargeService.class);

    @Autowired
    private LitemallUserService userService;

    @Autowired
    private LitemallRechargeService rechargeService;

    @Autowired
    private WxPayService wxPayService;

//    @Autowired
//    private LitemallUserFormIdService formIdService;

    @Autowired
    private NotifyService notifyService;

    public Object submit(Integer userId, String body, HttpServletRequest request) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        Double amount = JacksonUtil.parseObject(body, "amount", Double.class);
        if (amount == null) {
            return ResponseUtil.badArgument();
        }

        // 实付金额
        BigDecimal actualPrice = BigDecimal.valueOf(amount);

        // 赠送金额
        BigDecimal giftPrice = BigDecimal.valueOf(0.00);

        // 充值金额
        BigDecimal rechargePrice = actualPrice.add(giftPrice);

        Integer rechargeId = null;
        LitemallRecharge recharge = new LitemallRecharge();
        recharge.setUserId(userId);
        recharge.setRechargeSn(rechargeService.generateRechargeSn(userId));
        recharge.setRechargeStatus(OrderUtil.STATUS_CREATE);
        recharge.setGiftPrice(giftPrice);
        recharge.setActualPrice(actualPrice);
        recharge.setRechargePrice(rechargePrice);

        // 创建充值订单
        rechargeService.add(recharge);
        rechargeId = recharge.getId();

        LitemallUser user = userService.findById(userId);
        String openid = user.getWeixinOpenid();
        if (openid == null) {
            return ResponseUtil.fail(AUTH_OPENID_UNACCESS, "订单不能支付");
        }

        WxPayMpOrderResult result = null;
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setOutTradeNo(recharge.getRechargeSn());
            orderRequest.setOpenid(openid);
            orderRequest.setBody("订单：" + recharge.getRechargeSn());
            // 元转成分
            int fee = actualPrice.multiply(new BigDecimal(100)).intValue();
            orderRequest.setTotalFee(fee);
            orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));

            orderRequest.setNotifyUrl("https://api.jigeshuiguo.com/wx/recharge/pay-notify");

            result = wxPayService.createOrder(orderRequest);

/*            //缓存prepayID用于后续模版通知
            String prepayId = result.getPackageValue();
            prepayId = prepayId.replace("prepay_id=", "");
            LitemallUserFormid userFormid = new LitemallUserFormid();
            userFormid.setOpenid(user.getWeixinOpenid());
            userFormid.setFormid(prepayId);
            userFormid.setIsprepay(true);
            userFormid.setUseamount(3);
            userFormid.setExpireTime(LocalDateTime.now().plusDays(7));
            formIdService.addUserFormid(userFormid);*/

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.fail(ORDER_PAY_FAIL, "充值不能支付");
        }

        return ResponseUtil.ok(result);
    }

    /**
     * 微信付款成功或失败回调接口
     * <p>
     * 1. 检测当前订单是否是付款状态;
     * 2. 设置订单付款成功状态相关信息;
     * 3. 响应微信商户平台.
     *
     * @param request  请求内容
     * @param response 响应内容
     * @return 操作结果
     */
    @Transactional
    public Object payNotify(HttpServletRequest request, HttpServletResponse response) {
        String xmlResult = null;
        try {
            xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        } catch (IOException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        WxPayOrderNotifyResult result = null;
        try {
            result = wxPayService.parseOrderNotifyResult(xmlResult);

            if (!WxPayConstants.ResultCode.SUCCESS.equals(result.getResultCode())) {
                logger.error(xmlResult);
                throw new WxPayException("微信通知支付失败！");
            }
            if (!WxPayConstants.ResultCode.SUCCESS.equals(result.getReturnCode())) {
                logger.error(xmlResult);
                throw new WxPayException("微信通知支付失败！");
            }
        } catch (WxPayException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        logger.info("处理腾讯支付平台的订单支付");
        logger.info(result);

        String orderSn = result.getOutTradeNo();
        String payId = result.getTransactionId();

        // 分转化成元
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
        LitemallRecharge recharge = rechargeService.findBySn(orderSn);
        if (recharge == null) {
            return WxPayNotifyResponse.fail("订单不存在 sn=" + orderSn);
        }

        // 检查这个订单是否已经处理过
        if (isPayStatus(recharge) && recharge.getPayId() != null) {
            return WxPayNotifyResponse.success("订单已经处理成功!");
        }

        // 检查支付订单金额
        if (!totalFee.equals(recharge.getActualPrice().toString())) {
            return WxPayNotifyResponse.fail(recharge.getRechargeSn() + " : 支付金额不符合 totalFee=" + totalFee);
        }

        recharge.setPayId(payId);
        recharge.setPayTime(LocalDateTime.now());
        recharge.setRechargeStatus(OrderUtil.STATUS_PAY);
        if (rechargeService.updateWithOptimisticLocker(recharge) == 0) {
            return WxPayNotifyResponse.fail("更新数据已失效");
        }

        // 更新用户账户余额
        LitemallUser user = userService.findById(recharge.getUserId());
        if (userService.updateUserBalanceById(user.getId(), recharge.getRechargePrice().add(user.getBalance())) == 0) {
            return WxPayNotifyResponse.fail("更新数据已失效");
        }

        // 请依据自己的模版消息配置更改参数
        String[] parms = new String[]{
                recharge.getRechargeSn(),
                recharge.getRechargePrice().toString(),
                DateTimeUtil.getDateTimeDisplayString(recharge.getAddTime())
        };

//        notifyService.notifyWxTemplate(result.getOpenid(), NotifyType.PAY_SUCCEED, parms,
//                "pages/index/index?rechargeId=" + recharge.getId());

        return WxPayNotifyResponse.success("处理成功!");
    }

    private static boolean isPayStatus(LitemallRecharge recharge) {
        return OrderUtil.STATUS_PAY == recharge.getRechargeStatus().shortValue();
    }
}
