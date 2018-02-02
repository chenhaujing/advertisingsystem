package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by szty on 2017/8/7.
 */
@Entity
public class StoreCategoryStandard extends BaseModel {
    /**
     * 门店类别标准名称
     */
    @Column(nullable = false, length = 128)
    private String storeCategoryStandardName;
    /**
     * 类别类型
     */
    @Column(nullable = false)
    private Integer storeType;
    /**
     * 商品数量范围最小值
     */
    @Column(nullable = false)
    private Integer commodityAmountMin;

    /**
     * 商品数量范围最大值
     */
    @Column(nullable = false)
    private Integer commodityAmountMax;
    /**
     * 平均每日交易订单数范围最小值
     */
    @Column(nullable = false)
    private Integer avgDailyTradingAmountMin;

    /**
     * 平均每日交易订单数范围最大值
     */
    @Column(nullable = false)
    private Integer avgDailyTradingAmountMax;

    public String getStoreCategoryStandardName() {
        return storeCategoryStandardName;
    }

    public void setStoreCategoryStandardName(String storeCategoryStandardName) {
        this.storeCategoryStandardName = storeCategoryStandardName;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public Integer getCommodityAmountMin() {
        return commodityAmountMin;
    }

    public void setCommodityAmountMin(Integer commodityAmountMin) {
        this.commodityAmountMin = commodityAmountMin;
    }

    public Integer getCommodityAmountMax() {
        return commodityAmountMax;
    }

    public void setCommodityAmountMax(Integer commodityAmountMax) {
        this.commodityAmountMax = commodityAmountMax;
    }

    public Integer getAvgDailyTradingAmountMin() {
        return avgDailyTradingAmountMin;
    }

    public void setAvgDailyTradingAmountMin(Integer avgDailyTradingAmountMin) {
        this.avgDailyTradingAmountMin = avgDailyTradingAmountMin;
    }

    public Integer getAvgDailyTradingAmountMax() {
        return avgDailyTradingAmountMax;
    }

    public void setAvgDailyTradingAmountMax(Integer avgDailyTradingAmountMax) {
        this.avgDailyTradingAmountMax = avgDailyTradingAmountMax;
    }
}