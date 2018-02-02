package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.advertisement.material.AdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.utils.EnumUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wenfeng on 2017/9/4.
 */
@Entity
public class Advertisement extends BaseModel {

    @Column(name = "contract_id",updatable = false,length = 36)
    private String contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id",insertable = false,updatable = false)
    private Contract contract;

    /**
     * 素材ID
     */
    @Column(name = "material_id",length = 36)
    private String materialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id",insertable = false,updatable = false)
    private AdvertisementMaterial advertisementMaterial;


    @Transient
    private String data;

    /**
     * 客户ID
     */
    @Column(name = "customer_id",nullable = false,updatable = false,length = 36)
    private String customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",insertable = false,updatable = false)
    private Customer customer;

    @Column(nullable = false)
    private Integer advertisementType=1;

    @Column(nullable = false)
    private String advertisementName;

    /**
     * 广告预期开始投放时间
     */
    @Column(nullable = false)
    private Date startTime;

    /**
     * 广告预期结束投放时间
     */
    @Column(nullable = false)
    private Date endTime;

    @Column(length = 1024)
    private String remark;

    @Column(nullable = false,columnDefinition = "Integer default  1")
    private Integer advertisementStatus=1;

    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer advertisementPeriod=0;

    /**
     * 实际投放开始时间
     */
    @Column
    private Date effectiveStartTime;

    /**
     * 实际投放截止时间
     */
    @Column
    private Date effectiveEndTime;


    /**
     * 实际投放周期
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer effectivePeriod=0;

    /**
     * 投放后的预计截止时间
     */
    @Column
    private Date expectedDueDay;

    /**
     * 投放人
     */
    @Column(name = "delivery_operator_id",length = 36)
    private String deliveryOperatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_operator_id",insertable = false,updatable = false)
    private User deliveryOperator;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        this.advertisementName = advertisementName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Integer getAdvertisementStatus() {
        return advertisementStatus;
    }

    public void setAdvertisementStatus(Integer advertisementStatus) {
        this.advertisementStatus = advertisementStatus;
    }

    public Integer getAdvertisementType() {
        return advertisementType;
    }

    public void setAdvertisementType(Integer advertisementType) {
        this.advertisementType = advertisementType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getData() {
        if(data ==null){
            data = advertisementMaterial.getData();
        }
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public AdvertisementMaterial getAdvertisementMaterial() {
        return advertisementMaterial;
    }

    public void setAdvertisementMaterial(AdvertisementMaterial advertisementMaterial) {
        this.advertisementMaterial = advertisementMaterial;
    }

    public Integer getAdvertisementPeriod() {
        return advertisementPeriod;
    }

    public void setAdvertisementPeriod(Integer advertisementPeriod) {
        this.advertisementPeriod = advertisementPeriod;
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

    public Integer getEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(Integer effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }

    public String getDeliveryOperatorId() {
        return deliveryOperatorId;
    }

    public void setDeliveryOperatorId(String deliveryOperatorId) {
        this.deliveryOperatorId = deliveryOperatorId;
    }

    public User getDeliveryOperator() {
        return deliveryOperator;
    }

    public void setDeliveryOperator(User deliveryOperator) {
        this.deliveryOperator = deliveryOperator;
    }

    public Date getExpectedDueDay() {
        return expectedDueDay;
    }

    public void setExpectedDueDay(Date expectedDueDay) {
        this.expectedDueDay = expectedDueDay;
    }

    public AdvertisementStatusEnum  getAdvertisementStatusEnum(){
        return EnumUtils.toEnum(getAdvertisementStatus(),AdvertisementStatusEnum.class);
    }
}
