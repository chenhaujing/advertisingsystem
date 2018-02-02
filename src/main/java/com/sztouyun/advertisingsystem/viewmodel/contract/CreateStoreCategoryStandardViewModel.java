package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class CreateStoreCategoryStandardViewModel {

    @ApiModelProperty(value = "门店类别标准名称", required = true)
    @Size(max = 128,message ="门店类别标准名称太长" )
    @NotBlank(message = "门店类别标准名称不能为空")
    private String storeCategoryStandardName;

    @ApiModelProperty(value = "类别类型", required = true)
    @NotNull(message = "类别类型不能为空")
    private Integer storeType;

    @ApiModelProperty(value = "商品数量最小值", required = true)
    @Max(value= Constant.INTEGER_MAX,message = "商品数量最小值的最大值不能超过999999999")
    @Min(value = 0, message = "商品数量不能小于0")
    @NotNull(message = "商品数量最小值不能为空")
    private Integer commodityAmountMin;

    @ApiModelProperty(value = "商品数量最大值(如果是无穷大，填写：999999)", required = true)
    @Max(value= Constant.INTEGER_MAX,message = "商品数量最大值不能超过999999999")
    @Min(value = 0, message = "商品数量不能小于0")
    @NotNull(message = "商品数量最大值不能为空")
    private Integer commodityAmountMax;

    @ApiModelProperty(value = "平均每日交易订单数最小值", required = true)
    @Max(value= Constant.INTEGER_MAX,message = "平均每日交易订单数最小值的最大值不能超过999999999")
    @Min(value = 0, message = "平均每日交易订单数不能小于0")
    @NotNull(message = "平均每日交易订单数最小值不能为空")
    private Integer avgDailyTradingAmountMin;

    @ApiModelProperty(value = "平均每日交易订单数最大值(如果是无穷大，填写：999999)", required = true)
    @Max(value= Constant.INTEGER_MAX,message = "平均每日交易订单数最大值不能超过999999999")
    @Min(value = 0, message = "平均每日交易订单数不能小于0")
    @NotNull(message = "平均每日交易订单数最大值不能为空")
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
