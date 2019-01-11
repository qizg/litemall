package org.linlinjava.litemall.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallFlashSalesMapper;
import org.linlinjava.litemall.db.domain.LitemallFlashSales;
import org.linlinjava.litemall.db.domain.LitemallFlashSalesExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LitemallFlashSalesService {
    @Resource
    private LitemallFlashSalesMapper mapper;

    /**
     * 获取用户参与的抢购记录
     *
     * @param userId
     * @return
     */
    public List<LitemallFlashSales> queryMyJoinGroupon(Integer userId) {
        LitemallFlashSalesExample example = new LitemallFlashSalesExample();
        example.or().andUserIdEqualTo(userId)
                .andDeletedEqualTo(false)
                .andPayedEqualTo(true);
        example.orderBy("add_time desc");
        return mapper.selectByExample(example);
    }

    /**
     * 根据OrderId查询抢购记录
     *
     * @param orderId
     * @return
     */
    public LitemallFlashSales queryByOrderId(Integer orderId) {
        LitemallFlashSalesExample example = new LitemallFlashSalesExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        return mapper.selectOneByExample(example);
    }

    /**
     * 获取某个抢购活动参与的记录
     *
     * @param id
     * @return
     */
    public List<LitemallFlashSales> queryJoinRecord(Integer id) {
        LitemallFlashSalesExample example = new LitemallFlashSalesExample();
        example.or().andRulesIdEqualTo(id).andDeletedEqualTo(false).andPayedEqualTo(true);
        example.orderBy("add_time desc");
        return mapper.selectByExample(example);
    }

    /**
     * 根据ID查询记录
     *
     * @param id
     * @return
     */
    public LitemallFlashSales queryById(Integer id) {
        LitemallFlashSalesExample example = new LitemallFlashSalesExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false).andPayedEqualTo(true);
        return mapper.selectOneByExample(example);
    }

    /**
     * 返回某个抢购参与数
     *
     * @param ruleId
     * @return
     */
    public int countFlashSales(Integer ruleId) {
        LitemallFlashSalesExample example = new LitemallFlashSalesExample();
        example.or().andRulesIdEqualTo(ruleId).andDeletedEqualTo(false).andPayedEqualTo(true);
        return (int) mapper.countByExample(example);
    }

    public int updateById(LitemallFlashSales flashSales) {
        flashSales.setUpdateTime(LocalDateTime.now());
        return mapper.updateByPrimaryKeySelective(flashSales);
    }

    /**
     * 创建或参与一个抢购
     *
     * @param flashSales
     * @return
     */
    public int createFlashSales(LitemallFlashSales flashSales) {
        flashSales.setAddTime(LocalDateTime.now());
        flashSales.setUpdateTime(LocalDateTime.now());
        return mapper.insertSelective(flashSales);
    }


    /**
     * 查询所有发起的抢购记录
     *
     * @param rulesId
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<LitemallFlashSales> querySelective(String rulesId, Integer page, Integer size, String sort, String order) {
        LitemallFlashSalesExample example = new LitemallFlashSalesExample();
        LitemallFlashSalesExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(rulesId)) {
            criteria.andRulesIdEqualTo(Integer.parseInt(rulesId));
        }
        criteria.andDeletedEqualTo(false);
        criteria.andPayedEqualTo(true);

        PageHelper.startPage(page, size);
        return mapper.selectByExample(example);
    }

    public int countSelective(String rulesId, Integer page, Integer limit, String sort, String order) {
        LitemallFlashSalesExample example = new LitemallFlashSalesExample();
        LitemallFlashSalesExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(rulesId)) {
            criteria.andRulesIdEqualTo(Integer.parseInt(rulesId));
        }
        criteria.andDeletedEqualTo(false);
        criteria.andPayedEqualTo(true);

        return (int) mapper.countByExample(example);
    }
}
