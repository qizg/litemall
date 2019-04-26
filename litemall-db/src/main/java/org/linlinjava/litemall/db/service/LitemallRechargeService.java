package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallRechargeMapper;
import org.linlinjava.litemall.db.dao.RechargeMapper;
import org.linlinjava.litemall.db.domain.LitemallRecharge;
import org.linlinjava.litemall.db.domain.LitemallRechargeExample;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class LitemallRechargeService {
    @Resource
    private LitemallRechargeMapper litemallRechargeMapper;
    @Resource
    private RechargeMapper rechargeMapper;

    public int add(LitemallRecharge recharge) {
        recharge.setAddTime(LocalDateTime.now());
        recharge.setUpdateTime(LocalDateTime.now());
        return litemallRechargeMapper.insertSelective(recharge);
    }

    public int count(Integer userId) {
        LitemallRechargeExample example = new LitemallRechargeExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return (int) litemallRechargeMapper.countByExample(example);
    }

    public LitemallRecharge findById(Integer rechargeId) {
        return litemallRechargeMapper.selectByPrimaryKey(rechargeId);
    }

    private String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public int countByRechargeSn(Integer userId, String rechargeSn) {
        LitemallRechargeExample example = new LitemallRechargeExample();
        example.or().andUserIdEqualTo(userId).andRechargeSnEqualTo(rechargeSn).andDeletedEqualTo(false);
        return (int) litemallRechargeMapper.countByExample(example);
    }

    // TODO 这里应该产生一个唯一的订单，但是实际上这里仍然存在两个订单相同的可能性
    public String generateRechargeSn(Integer userId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = df.format(LocalDate.now());
        String rechargeSn = now + getRandomNum(6);
        while (countByRechargeSn(userId, rechargeSn) != 0) {
            rechargeSn = getRandomNum(6);
        }
        return rechargeSn;
    }

    public List<LitemallRecharge> queryByRechargeStatus(Integer userId, List<Short> rechargeStatus,
                                                        Integer page, Integer limit) {
        LitemallRechargeExample example = new LitemallRechargeExample();
        example.setOrderByClause(LitemallRecharge.Column.addTime.desc());
        LitemallRechargeExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        if (rechargeStatus != null) {
            criteria.andRechargeStatusIn(rechargeStatus);
        }
        criteria.andDeletedEqualTo(false);
        PageHelper.startPage(page, limit);
        return litemallRechargeMapper.selectByExample(example);
    }

    public List<LitemallRecharge> querySelective(Integer userId, String rechargeSn,
                                                 List<Short> rechargeStatusArray,
                                                 Integer page, Integer limit,
                                                 String sort, String recharge) {
        LitemallRechargeExample example = new LitemallRechargeExample();
        LitemallRechargeExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(rechargeSn)) {
            criteria.andRechargeSnEqualTo(rechargeSn);
        }
        if (rechargeStatusArray != null && rechargeStatusArray.size() != 0) {
            criteria.andRechargeStatusIn(rechargeStatusArray);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(recharge)) {
            example.setOrderByClause(sort + " " + recharge);
        }

        PageHelper.startPage(page, limit);
        return litemallRechargeMapper.selectByExample(example);
    }

    public int updateWithOptimisticLocker(LitemallRecharge recharge) {
        LocalDateTime preUpdateTime = recharge.getUpdateTime();
        recharge.setUpdateTime(LocalDateTime.now());
        return rechargeMapper.updateWithOptimisticLocker(preUpdateTime, recharge);
    }

    public void deleteById(Integer id) {
        litemallRechargeMapper.logicalDeleteByPrimaryKey(id);
    }

    public int count() {
        LitemallRechargeExample example = new LitemallRechargeExample();
        example.or().andDeletedEqualTo(false);
        return (int) litemallRechargeMapper.countByExample(example);
    }

    public List<LitemallRecharge> queryUnpaid(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.minusMinutes(minutes);
        LitemallRechargeExample example = new LitemallRechargeExample();
        example.or().andRechargeStatusEqualTo(OrderUtil.STATUS_CREATE)
                .andAddTimeLessThan(expired).andDeletedEqualTo(false);
        return litemallRechargeMapper.selectByExample(example);
    }

    public LitemallRecharge findBySn(String orderSn) {
        LitemallRechargeExample example = new LitemallRechargeExample();
        example.or().andRechargeSnEqualTo(orderSn).andDeletedEqualTo(false);
        return litemallRechargeMapper.selectOneByExample(example);
    }

}
