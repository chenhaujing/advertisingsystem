package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.Between;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;
import java.util.Date;

@ApiModel
public class BaseContractViewModel {

    @ApiModelProperty(value = "合同名称", required = true)
    @Size(max = 128, message = "合同名称太长")
    @NotBlank(message = "合同名称不能为空")
    private String contractName;

    @ApiModelProperty(value = "客户id", required = true)
    @NotBlank(message = "客户id不能为空")
    private String customerId;

    @ApiModelProperty(value = "甲方名称", required = true)
    @Size(max = 128, message = "甲方名称太长")
    @NotBlank(message = "甲方名称不能为空")
    private String firstPartyName;

    @ApiModelProperty(value = "甲方责任联系人", required = true)
    @Size(max = 128, message = "甲方责任联系人太长")
    @NotBlank(message = "甲方责任联系人不能为空")
    private String firstPartyResponsibilityPerson;

    @ApiModelProperty(value = "甲方合同接收地址", required = true)
    @Size(max = 128, message = "甲方责任联系人太长")
    @NotBlank(message = "甲方合同接收地址不能为空")
    private String firstPartyContractReceiveAddress;

    @ApiModelProperty(value = "甲方邮件信息", required = true)
    @Pattern(regexp = Constant.REGEX_EMAIL, message = "甲方邮箱格式错误！")
    @Size(max = 128, message = "甲方邮件信息太长")
    @NotBlank(message = "甲方邮件地址不能为空")
    private String firstPartyEmail;

    @ApiModelProperty(value = "甲方电话信息", required = true)
    @Size(max = 20, message = "甲方电话信息太长")
    @Pattern(regexp = Constant.REGEX_PHONE, message = "甲方电话信息错误！")
    private String firstPartyPhone;

    @ApiModelProperty(value = "乙方名称", required = true)
    @Size(max = 128, message = "乙方名称太长")
    @NotBlank(message = "乙方名称不能为空")
    private String secondPartyName;

    @ApiModelProperty(value = "乙方责任联系人", required = true)
    @Size(max = 128, message = "乙方名称太长")
    @NotBlank(message = "乙方责任联系人不能为空")
    private String secondPartyResponsibilityPerson;

    @ApiModelProperty(value = "乙方合同接收地址", required = true)
    @Size(max = 128, message = "乙方合同接收地址太长")
    @NotBlank(message = "乙方合同接收地址不能为空")
    private String secondPartyContractReceiveAddress;

    @ApiModelProperty(value = "乙方邮件信息", required = true)
    @Pattern(regexp = Constant.REGEX_EMAIL, message = "乙方邮箱格式错误！")
    @Size(max = 128, message = "甲方邮件信息太长")
    @NotBlank(message = "乙方邮件地址不能为空")
    private String secondPartyEmail;

    @ApiModelProperty(value = "乙方电话信息", required = true)
    @Size(max = 20, message = "乙方电话信息太长")
    @Pattern(regexp = Constant.REGEX_PHONE, message = "乙方电话信息错误！")
    private String secondPartyPhone;

    @ApiModelProperty(value = "媒体费用", required = true)
    @Max(value = Constant.INTEGER_MAX, message = "媒体费用最大值不能超过999999999")
    @Min(value = 0, message = "媒体费用不能小于0")
    @NotNull(message = "媒体费用不能为空")
    private Double mediumCost;

    @ApiModelProperty(value = "制作费用", required = true)
    @Max(value = Constant.INTEGER_MAX, message = "制作费用最大值不能超过999999999")
    @Min(value = 0, message = "制作费用不能小于0")
    @NotNull(message = "制作费用不能为空")
    private Double productCost;

    @ApiModelProperty(value = "前述费用支付天数", required = true)
    @Max(value = Constant.INTEGER_MAX, message = "前述费用支付天数最大值不能超过999999999")
    @Min(value = 0, message = "前述费用支付天数不能小于0")
    @NotNull(message = "前述费用不能为空")
    private Integer signAfterDay;

    @ApiModelProperty(value = "银行账户名称", required = true)
    @Size(max = 128, message = "银行账户名称太长")
    @NotBlank(message = "银行账户名称不能为空")
    private String bankAccountName;

    @ApiModelProperty(value = "银行账户号码", required = true)
    @Pattern(regexp = Constant.REGEX_BANK_NUMBER, message = "银行账户号码格式错误！")
    @NotBlank(message = "银行账户号码不能为空")
    @Size(max = 30,message = "银行账户号码超过长度")
    private String bankAccountNumber;

    @ApiModelProperty(value = "银行名称", required = true)
    @Size(max = 128, message = "银行名称太长")
    @NotBlank(message = "银行名称不能为空")
    private String bankName;

    @ApiModelProperty(value = "合作周期开始时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "合作周期开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "合作周期截止时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "合作周期截止时间不能为空")
    private Date endTime;

    @ApiModelProperty(value = "合作周期总月数", required = true)
    @Max(value = Constant.INTEGER_MAX, message = "合作周期总月数最大值不能超过999999999")
    @Min(value = 0, message = "合作周期总月数不能小于0")
    @NotNull(message = "合作周期不能为空")
    private Integer totalMonths;

    @ApiModelProperty(value = "备注")
    @Size(max = 2000, message = "备注太长")
    private String remark;

    @ApiModelProperty(value = "A类门店数量")
    @Max(value = Constant.INTEGER_MAX, message = "A类门店数量最大值不能超过999999999")
    @Min(value = 0, message = "A类门店数量不能小于0")
    @NotNull(message = "A类门店数量不能为空")
    private Integer storeACount;

    @ApiModelProperty(value = "B类门店数量")
    @Max(value = Constant.INTEGER_MAX, message = "B类门店数量最大值不能超过999999999")
    @Min(value = 0, message = "B类门店数量不能小于0")
    @NotNull(message = "B类门店数量不能为空")
    private Integer storeBCount;

    @ApiModelProperty(value = "C类门店数量")
    @Max(value = Constant.INTEGER_MAX, message = "C类门店数量最大值不能超过999999999")
    @Min(value = 0, message = "C类门店数量不能小于0")
    @NotNull(message = "C类门店数量不能为空")
    private Integer storeCCount;

    @ApiModelProperty(value = "费用折扣, 保留两位小数")
    @Between(min = 0, max = 1, includeMin = false, message = "折扣必须在0和1之间(不包括0)")
    private Double discount;

    @ApiModelProperty(value = "广告尺寸配置id")
    @NotBlank(message = "请选择正确的广告尺寸")
    private String advertisementSizeConfigId;

    @ApiModelProperty(value = "广告时长配置id")
    @NotBlank(message = "请选择正确的广告时长")
    private String advertisementDurationConfigId;

    @ApiModelProperty(value = "广告展示次数配置id")
    @NotBlank(message = "请选择正确的广告展示次数")
    private String advertisementDisplayTimesConfigId;

    public String getAdvertisementSizeConfigId() {
        return advertisementSizeConfigId;
    }

    public void setAdvertisementSizeConfigId(String advertisementSizeConfigId) {
        this.advertisementSizeConfigId = advertisementSizeConfigId;
    }

    public String getAdvertisementDurationConfigId() {
        return advertisementDurationConfigId;
    }

    public void setAdvertisementDurationConfigId(String advertisementDurationConfigId) {
        this.advertisementDurationConfigId = advertisementDurationConfigId;
    }

    public String getAdvertisementDisplayTimesConfigId() {
        return advertisementDisplayTimesConfigId;
    }

    public void setAdvertisementDisplayTimesConfigId(String advertisementDisplayTimesConfigId) {
        this.advertisementDisplayTimesConfigId = advertisementDisplayTimesConfigId;
    }

    public Integer getStoreACount() {
        if (storeACount == null)
            return 0;
        return storeACount;
    }

    public void setStoreACount(Integer storeACount) {
        this.storeACount = storeACount;
    }

    public Integer getStoreBCount() {
        if (storeBCount == null)
            return 0;
        return storeBCount;
    }

    public void setStoreBCount(Integer storeBCount) {
        this.storeBCount = storeBCount;
    }

    public Integer getStoreCCount() {
        if (storeCCount == null)
            return 0;
        return storeCCount;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFirstPartyName() {
        return firstPartyName;
    }

    public void setFirstPartyName(String firstPartyName) {
        this.firstPartyName = firstPartyName;
    }

    public String getFirstPartyResponsibilityPerson() {
        return firstPartyResponsibilityPerson;
    }

    public void setFirstPartyResponsibilityPerson(String firstPartyResponsibilityPerson) {
        this.firstPartyResponsibilityPerson = firstPartyResponsibilityPerson;
    }

    public String getFirstPartyContractReceiveAddress() {
        return firstPartyContractReceiveAddress;
    }

    public void setFirstPartyContractReceiveAddress(String firstPartyContractReceiveAddress) {
        this.firstPartyContractReceiveAddress = firstPartyContractReceiveAddress;
    }

    public String getFirstPartyEmail() {
        return firstPartyEmail;
    }

    public void setFirstPartyEmail(String firstPartyEmail) {
        this.firstPartyEmail = firstPartyEmail;
    }

    public String getFirstPartyPhone() {
        return firstPartyPhone;
    }

    public void setFirstPartyPhone(String firstPartyPhone) {
        this.firstPartyPhone = firstPartyPhone;
    }

    public String getSecondPartyName() {
        return secondPartyName;
    }

    public void setSecondPartyName(String secondPartyName) {
        this.secondPartyName = secondPartyName;
    }

    public String getSecondPartyResponsibilityPerson() {
        return secondPartyResponsibilityPerson;
    }

    public void setSecondPartyResponsibilityPerson(String secondPartyResponsibilityPerson) {
        this.secondPartyResponsibilityPerson = secondPartyResponsibilityPerson;
    }

    public String getSecondPartyContractReceiveAddress() {
        return secondPartyContractReceiveAddress;
    }

    public void setSecondPartyContractReceiveAddress(String secondPartyContractReceiveAddress) {
        this.secondPartyContractReceiveAddress = secondPartyContractReceiveAddress;
    }

    public String getSecondPartyEmail() {
        return secondPartyEmail;
    }

    public void setSecondPartyEmail(String secondPartyEmail) {
        this.secondPartyEmail = secondPartyEmail;
    }

    public String getSecondPartyPhone() {
        return secondPartyPhone;
    }

    public void setSecondPartyPhone(String secondPartyPhone) {
        this.secondPartyPhone = secondPartyPhone;
    }

    public Double getMediumCost() {
        return mediumCost;
    }

    public void setMediumCost(Double mediumCost) {
        this.mediumCost = mediumCost;
    }

    public Double getProductCost() {
        return productCost;
    }

    public void setProductCost(Double productCost) {
        this.productCost = productCost;
    }

    public Integer getSignAfterDay() {
        return signAfterDay;
    }

    public void setSignAfterDay(Integer signAfterDay) {
        this.signAfterDay = signAfterDay;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalMonths() {
        return totalMonths;
    }

    public void setTotalMonths(Integer totalMonths) {
        this.totalMonths = totalMonths;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setStoreCCount(Integer storeCCount) {
        this.storeCCount = storeCCount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
