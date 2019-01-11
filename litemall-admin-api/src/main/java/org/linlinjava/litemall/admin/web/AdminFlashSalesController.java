package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallFlashSales;
import org.linlinjava.litemall.db.domain.LitemallFlashSalesRules;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.service.LitemallFlashSalesRulesService;
import org.linlinjava.litemall.db.service.LitemallFlashSalesService;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/flashsales")
@Validated
public class AdminFlashSalesController {
    private final Log logger = LogFactory.getLog(AdminFlashSalesController.class);

    @Autowired
    private LitemallFlashSalesRulesService rulesService;
    @Autowired
    private LitemallGoodsService goodsService;
    @Autowired
    private LitemallFlashSalesService flashSalesService;

    @RequiresPermissions("admin:flashsales:read")
    @RequiresPermissionsDesc(menu={"推广管理" , "抢购管理"}, button="查询")
    @GetMapping("/listRecord")
    public Object listRecord(String flashSalesId,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit,
                             @Sort @RequestParam(defaultValue = "add_time") String sort,
                             @Order @RequestParam(defaultValue = "desc") String order) {
        List<LitemallFlashSales> flashSalesList = flashSalesService.querySelective(flashSalesId, page, limit, sort, order);
        int total = flashSalesService.countSelective(flashSalesId, page, limit, sort, order);

        List<Map<String, Object>> records = new ArrayList<>();
        for (LitemallFlashSales flashSales : flashSalesList) {
            try {
                Map<String, Object> recordData = new HashMap<>();
                LitemallFlashSalesRules rules = rulesService.queryById(flashSales.getRulesId());
                LitemallGoods goods = goodsService.findById(rules.getGoodsId());

                recordData.put("flashSales", flashSales);
                recordData.put("rules", rules);
                recordData.put("goods", goods);

                records.add(recordData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", records);

        return ResponseUtil.ok(data);
    }

    @RequiresPermissions("admin:flashsales:list")
    @RequiresPermissionsDesc(menu={"推广管理" , "抢购管理"}, button="查询")
    @GetMapping("/list")
    public Object list(String goodsId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LitemallFlashSalesRules> rulesList = rulesService.querySelective(goodsId, page, limit, sort, order);
        int total = rulesService.countSelective(goodsId, page, limit, sort, order);
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("items", rulesList);

        return ResponseUtil.ok(data);
    }

    private Object validate(LitemallFlashSalesRules flashSalesRules) {
        Integer goodsId = flashSalesRules.getGoodsId();
        if (goodsId == null) {
            return ResponseUtil.badArgument();
        }
        BigDecimal discount = flashSalesRules.getDiscount();
        if (discount == null) {
            return ResponseUtil.badArgument();
        }
        Integer discountMember = flashSalesRules.getNumber();
        if (discountMember == null) {
            return ResponseUtil.badArgument();
        }
        LocalDateTime expireTime = flashSalesRules.getExpireTime();
        if (expireTime == null) {
            return ResponseUtil.badArgument();
        }

        return null;
    }

    @RequiresPermissions("admin:flashsales:update")
    @RequiresPermissionsDesc(menu={"推广管理" , "抢购管理"}, button="编辑")
    @PostMapping("/update")
    public Object update(@RequestBody LitemallFlashSalesRules flashSalesRules) {
        Object error = validate(flashSalesRules);
        if (error != null) {
            return error;
        }

        Integer goodsId = flashSalesRules.getGoodsId();
        LitemallGoods goods = goodsService.findById(goodsId);
        if (goods == null) {
            return ResponseUtil.badArgumentValue();
        }

        flashSalesRules.setGoodsName(goods.getName());
        flashSalesRules.setPicUrl(goods.getPicUrl());

        if (rulesService.updateById(flashSalesRules) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok();
    }


    @RequiresPermissions("admin:flashsales:create")
    @RequiresPermissionsDesc(menu={"推广管理" , "抢购管理"}, button="添加")
    @PostMapping("/create")
    public Object create(@RequestBody LitemallFlashSalesRules flashSalesRules) {
        Object error = validate(flashSalesRules);
        if (error != null) {
            return error;
        }

        Integer goodsId = flashSalesRules.getGoodsId();
        LitemallGoods goods = goodsService.findById(goodsId);
        if (goods == null) {
            return ResponseUtil.badArgumentValue();
        }

        flashSalesRules.setGoodsName(goods.getName());
        flashSalesRules.setPicUrl(goods.getPicUrl());

        rulesService.createRules(flashSalesRules);

        return ResponseUtil.ok(flashSalesRules);
    }


    @RequiresPermissions("admin:flashsales:delete")
    @RequiresPermissionsDesc(menu={"推广管理" , "抢购管理"}, button="删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody LitemallFlashSalesRules flashSalesRules) {
        Integer id = flashSalesRules.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }

        rulesService.delete(id);
        return ResponseUtil.ok();
    }
}
