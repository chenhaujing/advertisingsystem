package com.sztouyun.advertisingsystem.viewmodel.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by szty on 2017/7/25.
 */
@ApiModel
public class CustomerDetailViewModel extends UpdateCustomerViewModel{

    @ApiModelProperty(value = "所在省份")
    private String provinceName;

    @ApiModelProperty(value = "所在城市")
    private String cityName;

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "负责人名称")
    private String ownerName;

    @ApiModelProperty(value = "正在投放广告")
    private boolean delivering;

    @ApiModelProperty(value = "投放次数")
    private long advertisementDeliveryTimes = 0;

    @ApiModelProperty(value = "广告总额")
    private double advertisementTotalAmount = 0.00;

    @ApiModelProperty(value = "最后投放广告时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date lastestDeliveryTime;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isDelivering() {
        return delivering;
    }

    public void setDelivering(boolean delivering) {
        this.delivering = delivering;
    }

    public long getAdvertisementDeliveryTimes() {
        return advertisementDeliveryTimes;
    }

    public void setAdvertisementDeliveryTimes(long advertisementDeliveryTimes) {
        this.advertisementDeliveryTimes = advertisementDeliveryTimes;
    }

    public double getAdvertisementTotalAmount() {
        return advertisementTotalAmount;
    }

    public void setAdvertisementTotalAmount(double advertisementTotalAmount) {
        this.advertisementTotalAmount = advertisementTotalAmount;
    }

    public Date getLastestDeliveryTime() {
        return lastestDeliveryTime;
    }

    public void setLastestDeliveryTime(Date lastestDeliveryTime) {
        this.lastestDeliveryTime = lastestDeliveryTime;
    }
}
