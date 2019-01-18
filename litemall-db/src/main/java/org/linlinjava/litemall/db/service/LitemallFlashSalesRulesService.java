package org.linlinjava.litemall.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.FlashSalesRulesMapper;
import org.linlinjava.litemall.db.dao.LitemallFlashSalesRulesMapper;
import org.linlinjava.litemall.db.dao.LitemallGoodsMapper;
import org.linlinjava.litemall.db.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LitemallFlashSalesRulesService {
    @Resource
    private LitemallFlashSalesRulesMapper mapper;
    @Resource
    private LitemallGoodsMapper goodsMapper;
    @Resource
    private FlashSalesRulesMapper flashSalesRulesMapper;

    @Autowired
    private LitemallFlashSalesService flashSalesService;

    @Autowired
    private LitemallGoodsProductService goodsProductService;

    private LitemallGoods.Column[] goodsColumns = new LitemallGoods.Column[]{LitemallGoods.Column.id, LitemallGoods.Column.name, LitemallGoods.Column.brief, LitemallGoods.Column.picUrl, LitemallGoods.Column.counterPrice, LitemallGoods.Column.retailPrice};

    public int createRules(LitemallFlashSalesRules rules) {
        rules.setAddTime(LocalDateTime.now());
        rules.setUpdateTime(LocalDateTime.now());
        return mapper.insertSelective(rules);
    }

    /**
     * 根据ID查找对应抢购项
     *
     * @param id
     * @return
     */
    public LitemallFlashSalesRules queryById(Integer id) {
        LitemallFlashSalesRulesExample example = new LitemallFlashSalesRulesExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return mapper.selectOneByExample(example);
    }

    /**
     * 查询某个商品关联的抢购规则
     *
     * @param goodsId
     * @return
     */
    public List<LitemallFlashSalesRules> queryByGoodsId(Integer goodsId) {
        LitemallFlashSalesRulesExample example = new LitemallFlashSalesRulesExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return mapper.selectByExample(example);
    }

    /**
     * 查询某个商品关联的最新抢购规则
     *
     * @param goodsId
     * @return
     */
    public LitemallFlashSalesRulesResponse queryFirstByGoodsId(Integer goodsId) {
        LitemallFlashSalesRulesExample example = new LitemallFlashSalesRulesExample();
        example.or().andGoodsIdEqualTo(goodsId)
                .andDeletedEqualTo(false).
                andExpireTimeGreaterThan(LocalDateTime.now());
        example.setOrderByClause("expire_time desc");

        LitemallFlashSalesRules rules = mapper.selectOneByExample(example);
        if (rules == null) {
            return null;
        }
        LitemallFlashSalesRulesResponse response = new LitemallFlashSalesRulesResponse(rules);
        int soldNum = flashSalesService.countFlashSales(response.getId());
        response.setSoldNumber(soldNum);
        response.setLastNumber(response.getNumber() - soldNum);
        response.setExpireTimeMilliseconds(response.getExpireTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        return response;
    }

    /**
     * 获取首页抢购活动列表
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<Map<String, Object>> queryList(int offset, int limit) {
        return queryList(offset, limit, "expire_time", "asc");
    }

    public List<Map<String, Object>> queryList(int offset, int limit, String sort, String order) {
        LitemallFlashSalesRulesExample example = new LitemallFlashSalesRulesExample();
        example.or().andDeletedEqualTo(false).andExpireTimeGreaterThanOrEqualTo(LocalDateTime.now());
        example.setOrderByClause(sort + " " + order);
        Page page = PageHelper.startPage(offset, limit);
        page.setReasonable(false);
        List<LitemallFlashSalesRules> flashSalesRules = mapper.selectByExample(example);

        List<Map<String, Object>> flashSalesList = new ArrayList<>(flashSalesRules.size());
        for (LitemallFlashSalesRules rule : flashSalesRules) {
            Integer goodsId = rule.getGoodsId();
            LitemallGoods goods = goodsMapper.selectByPrimaryKeySelective(goodsId, goodsColumns);
            if (goods == null) {
                continue;
            }
            Map<String, Object> item = new HashMap<>(6);
            item.put("goods", goods);
            item.put("goods_product_id", goodsProductService.findOneByGoodsId(goods.getId()).getId());
            item.put("flash_sales_price", goods.getRetailPrice().subtract(rule.getDiscount()));
            int soldNum = flashSalesService.countFlashSales(rule.getId());
            item.put("sold_number", soldNum);
            item.put("last_number", rule.getNumber() - soldNum);
            item.put("expire_time", rule.getExpireTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
            flashSalesList.add(item);
        }

        return flashSalesList;
    }

    public int countList(int offset, int limit, String sort, String order) {
        LitemallFlashSalesRulesExample example = new LitemallFlashSalesRulesExample();
        example.or().andDeletedEqualTo(false);
        return (int) mapper.countByExample(example);
    }

    /**
     * 判断某个抢购活动是否已经过期
     *
     * @return
     */
    public boolean isExpired(LitemallGrouponRules rules) {
        return (rules == null || rules.getExpireTime().isBefore(LocalDateTime.now()));
    }

    /**
     * 获取抢购活动列表
     *
     * @param goodsId
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<LitemallFlashSalesRules> querySelective(String goodsId, Integer page, Integer size, String sort, String order) {
        LitemallFlashSalesRulesExample example = new LitemallFlashSalesRulesExample();
        example.setOrderByClause(sort + " " + order);

        setDeleteStatus(goodsId, example);

        PageHelper.startPage(page, size);
        return mapper.selectByExample(example);
    }

    private void setDeleteStatus(String goodsId, LitemallFlashSalesRulesExample example) {
        LitemallFlashSalesRulesExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(Integer.parseInt(goodsId));
        }
        criteria.andDeletedEqualTo(false);
    }

    public int countSelective(String goodsId, Integer page, Integer limit, String sort, String order) {
        LitemallFlashSalesRulesExample example = new LitemallFlashSalesRulesExample();
        setDeleteStatus(goodsId, example);

        return (int) mapper.countByExample(example);
    }

    public void delete(Integer id) {
        mapper.logicalDeleteByPrimaryKey(id);
    }

    public int updateById(LitemallFlashSalesRules flashSalesRules) {
        flashSalesRules.setUpdateTime(LocalDateTime.now());
        return mapper.updateByPrimaryKeySelective(flashSalesRules);
    }

    public int addStock(Integer id, Short num) {
        return flashSalesRulesMapper.addStock(id, num);
    }

    public int reduceStock(Integer id, Short num) {
        return flashSalesRulesMapper.reduceStock(id, num);
    }
}