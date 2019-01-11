package org.linlinjava.litemall.db.domain;

import java.math.BigDecimal;

public class LitemallFlashSalesRulesResponse extends LitemallFlashSalesRules {

    private BigDecimal flashSalesPrice;

    private int soldNumber;

    private int lastNumber;

    private long expireTimeMilliseconds;

    public LitemallFlashSalesRulesResponse(LitemallFlashSalesRules rules) {
        if (rules == null) {
            return;
        }
        setId(rules.getId());
        setGoodsId(rules.getGoodsId());
        setGoodsName(rules.getGoodsName());
        setPicUrl(rules.getPicUrl());
        setNumber(rules.getNumber());
        setDiscount(rules.getDiscount());
        setAddTime(rules.getAddTime());
        setUpdateTime(rules.getUpdateTime());
        setExpireTime(rules.getExpireTime());
        setDeleted(rules.getDeleted());
    }

    public BigDecimal getFlashSalesPrice() {
        return flashSalesPrice;
    }

    public void setFlashSalesPrice(BigDecimal flashSalesPrice) {
        this.flashSalesPrice = flashSalesPrice;
    }

    public int getSoldNumber() {
        return soldNumber;
    }

    public void setSoldNumber(int soldNumber) {
        this.soldNumber = soldNumber;
    }

    public int getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(int lastNumber) {
        this.lastNumber = lastNumber;
    }

    public long getExpireTimeMilliseconds() {
        return expireTimeMilliseconds;
    }

    public void setExpireTimeMilliseconds(long expireTimeMilliseconds) {
        this.expireTimeMilliseconds = expireTimeMilliseconds;
    }
}