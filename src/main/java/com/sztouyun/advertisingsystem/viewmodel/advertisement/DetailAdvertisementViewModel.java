package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wenfeng on 2017/9/8.
 */
@ApiModel
public class DetailAdvertisementViewModel extends UpdateAdvertisementViewModel{
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户头像")
    private String portrait;

    @ApiModelProperty(value = "广告创建时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN,timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "广告状态")
    private Integer advertisementStatus;

    @ApiModelProperty(value = "广告状态名称")
    private String advertisementStatusName;

    @ApiModelProperty(value = "广告创建人")
    private String creator;

    @ApiModelProperty(value = "广告客户名称")
    private String customerName;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "广告合同名称")
    private String contractName;

    @ApiModelProperty(value = "广告总价")
    private Double totalCost;

    @ApiModelProperty(value = "是否折扣")
    private Integer isDiscount=0;

    @ApiModelProperty(value = "折扣比例")
    private Double discount;

    @ApiModelProperty(value = "投放门店总数")
    private Integer totalStoreCount;

    @ApiModelProperty(value = "A类门店总数")
    private Integer storeACount;

    @ApiModelProperty(value = "B类门店总数")
    private Integer storeBCount;

    @ApiModelProperty(value = "C类门店总数")
    private Integer storeCCount;

    @ApiModelProperty(value = "合同周期开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN,timezone = "GMT+8")
    private Date startTimeOfContract;

    @ApiModelProperty(value = "合同周期结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN,timezone = "GMT+8")
    private Date endTimeOfContract;

    @ApiModelProperty(value = "合同周期")
    private String contractPeriod;

    @ApiModelProperty(value = "合同已投放周期")
    private String usedContractPeriod;

    @ApiModelProperty(value = "广告投放周期")
    private String advertisementPeriod;

    @ApiModelProperty(value = "广告位置")
    private String advertisementPosition;

    @ApiModelProperty(value = "广告尺寸")
    private String advertisementSize;

    @ApiModelProperty(value = "广告时长")
    private String advertisementDuration;

    @ApiModelProperty(value = "广告展示次数")
    private String getAdvertisementDisplayTimes;

    @ApiModelProperty(value = "广告类型")
    private Integer advertisementType;

    @ApiModelProperty(value = "广告类型名称")
    private String advertisementTypeName;

    @ApiModelProperty(value = "广告开始投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN,timezone = "GMT+8")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "广告结束投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN,timezone = "GMT+8")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "广告下架时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN,timezone = "GMT+8")
    private Date takeOffTime;

    @ApiModelProperty(value = "投放人")
    private String deliveryOperator;

    @ApiModelProperty(value = "下架人")
    private String takeOffOperator;

    @ApiModelProperty(value = "下架备注")
    private String takeOffRemark;
    
    @ApiModelProperty(value = "广告开始时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN,timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "广告截止时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date endTime;

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(Integer isDiscount) {
        this.isDiscount = isDiscount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getTotalStoreCount() {
        return totalStoreCount;
    }

    public void setTotalStoreCount(Integer totalStoreCount) {
        this.totalStoreCount = totalStoreCount;
    }

    public Integer getStoreACount() {
        return storeACount;
    }

    public void setStoreACount(Integer storeACount) {
        this.storeACount = storeACount;
    }

    public Integer getStoreBCount() {
        return storeBCount;
    }

    public void setStoreBCount(Integer storeBCount) {
        this.storeBCount = storeBCount;
    }

    public Integer getStoreCCount() {
        return storeCCount;
    }

    public void setStoreCCount(Integer storeCCount) {
        this.storeCCount = storeCCount;
    }

    public Date getStartTimeOfContract() {
        return startTimeOfContract;
    }

    public void setStartTimeOfContract(Date startTimeOfContract) {
        this.startTimeOfContract = startTimeOfContract;
    }

    public Date getEndTimeOfContract() {
        return endTimeOfContract;
    }

    public void setEndTimeOfContract(Date endTimeOfContract) {
        this.endTimeOfContract = endTimeOfContract;
    }

    public String getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(String contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public String getUsedContractPeriod() {
        return usedContractPeriod;
    }

    public void setUsedContractPeriod(String usedContractPeriod) {
        this.usedContractPeriod = usedContractPeriod;
    }

    public String getAdvertisementPeriod() {
        return advertisementPeriod;
    }

    public void setAdvertisementPeriod(String advertisementPeriod) {
        this.advertisementPeriod = advertisementPeriod;
    }

    public String getAdvertisementPosition() {
        return advertisementPosition;
    }

    public void setAdvertisementPosition(String advertisementPosition) {
        this.advertisementPosition = advertisementPosition;
    }

    public String getAdvertisementSize() {
        return advertisementSize;
    }

    public void setAdvertisementSize(String advertisementSize) {
        this.advertisementSize = advertisementSize;
    }

    public String getAdvertisementDuration() {
        return advertisementDuration;
    }

    public void setAdvertisementDuration(String advertisementDuration) {
        this.advertisementDuration = advertisementDuration;
    }

    public String getGetAdvertisementDisplayTimes() {
        return getAdvertisementDisplayTimes;
    }

    public void setGetAdvertisementDisplayTimes(String getAdvertisementDisplayTimes) {
        this.getAdvertisementDisplayTimes = getAdvertisementDisplayTimes;
    }

    @Override
    public Integer getAdvertisementType() {
        return advertisementType;
    }

    @Override
    public void setAdvertisementType(Integer advertisementType) {
        this.advertisementType = advertisementType;
    }

    public String getAdvertisementTypeName() {
        return advertisementTypeName;
    }

    public void setAdvertisementTypeName(String advertisementTypeName) {
        this.advertisementTypeName = advertisementTypeName;
    }

    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public Date getTakeOffTime() {
        return takeOffTime;
    }

    public void setTakeOffTime(Date takeOffTime) {
        this.takeOffTime = takeOffTime;
    }

    public String getDeliveryOperator() {
        return deliveryOperator;
    }

    public void setDeliveryOperator(String deliveryOperator) {
        this.deliveryOperator = deliveryOperator;
    }

    public String getTakeOffOperator() {
        return takeOffOperator;
    }

    public void setTakeOffOperator(String takeOffOperator) {
        this.takeOffOperator = takeOffOperator;
    }

    public String getTakeOffRemark() {
        return takeOffRemark;
    }

    public void setTakeOffRemark(String takeOffRemark) {
        this.takeOffRemark = takeOffRemark;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Integer getAdvertisementStatus() {
        return advertisementStatus;
    }

    public void setAdvertisementStatus(Integer advertisementStatus) {
        this.advertisementStatus = advertisementStatus;
    }

    public String getAdvertisementStatusName() {
        return advertisementStatusName;
    }

    public void setAdvertisementStatusName(String advertisementStatusName) {
        this.advertisementStatusName = advertisementStatusName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
