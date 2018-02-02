package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;


/**
 * Created by szty on 2017/8/2.
 */
@ApiModel
public class ContractDetailViewModel extends BaseContractViewModel{

    @ApiModelProperty(value = "合同ID")
    private String id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约时间")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date signTime;

    @ApiModelProperty(value = "合同状态名称")
    private String contractStatusName;

    @ApiModelProperty(value = "合同状态")
    private Integer contractStatus;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "签约人")
    private String signer;

    @ApiModelProperty(value = "维护人")
    private String owner;

    @ApiModelProperty(value = "广告总额")
    private double totalCost;

    @ApiModelProperty(value = "广告位总数")
    private int totalStore;

    @ApiModelProperty(value = "客户头像")
    private String headPortrait;

    @ApiModelProperty(value = "折扣金额")
    private double discountMoney;

    @ApiModelProperty(value = "合作周期开始时间", required = true)
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "合作周期截止时间", required = true)
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "广告配置尺寸名称")
    private String sizeName;

    @ApiModelProperty(value = "广告配置时长")
    private Integer duration;

    @ApiModelProperty(value = "广告配置时长单位名称")
    private String durationUnitName;

    @ApiModelProperty(value = "广告配置展示次数")
    private Integer  displayTimes;

    @ApiModelProperty(value = "广告配置展示次数单位名称")
    private String timeUnitName;

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


    public Integer getDisplayTimes() {
        return displayTimes;
    }

    public void setDisplayTimes(Integer displayTimes) {
        this.displayTimes = displayTimes;
    }

    public String getDurationUnitName() {
        return durationUnitName;
    }

    public void setDurationUnitName(String durationUnitName) {
        this.durationUnitName = durationUnitName;
    }

    public String getTimeUnitName() {
        return timeUnitName;
    }

    public void setTimeUnitName(String timeUnitName) {
        this.timeUnitName = timeUnitName;
    }

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

    public String getId() {
        return id;
    }

    public double getDiscountMoney() {
        return this.discountMoney;
    }

    public void setDiscountMoney(double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getContractStatusName() {
        return contractStatusName;
    }

    public void setContractStatusName(String contractStatusName) {
        this.contractStatusName = contractStatusName;
    }

    public Integer getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getTotalStore() {
        return totalStore;
    }

    public void setTotalStore(int totalStore) {
        this.totalStore = totalStore;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }
}
