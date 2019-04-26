package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.WxRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/wx/recharge")
@Validated
public class WxRechargeController {

    private final Log logger = LogFactory.getLog(WxOrderController.class);

    @Autowired
    private WxRechargeService rechargeService;

    /**
     * 充值提交
     *
     * @param userId 用户ID
     * @param body   充值信息，{ amount：xxx }
     * @return 充值订单ID
     */
    @PostMapping("submit")
    public Object submit(@LoginUser Integer userId, @RequestBody String body, HttpServletRequest request) {
        return rechargeService.submit(userId, body, request);
    }

    /**
     * 微信付款成功或失败回调接口
     *
     * @param request  请求内容
     * @param response 响应内容
     * @return 操作结果
     */
    @PostMapping("pay-notify")
    public Object payNotify(HttpServletRequest request, HttpServletResponse response) {
        return rechargeService.payNotify(request, response);
    }
}
