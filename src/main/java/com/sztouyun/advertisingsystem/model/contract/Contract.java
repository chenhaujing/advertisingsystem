package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.utils.EnumUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
public class Contract extends BaseModel {
    /**
     * 合同编码
     */
    @Column(nullable = false, length = 128)
    private String contractCode;

    /**
     * 合同名称
     */
    @Column(nullable = false, length = 128)
    private String contractName;

    /**
     * 客户id
     */
    @Column(name = "customer_id",updatable = false,length = 36)
    private String customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",insertable = false,updatable = false)
    private Customer customer;

    /**
     * 负责人
     */
    @Column(name = "owner_id",updatable = false,length = 36)
    private String ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false,updatable = false)
    private User owner;

    /**
     * 签约人
     */
    @Column(name = "signer_id",length = 36)
    private String signerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signer_id",insertable = false, updatable = false)
    private User signer;

    /**
     * 合同状态
     */
    @Column(nullable = false)
    private Integer contractStatus;

    /**
     * 签约时间
     */
    @Column
    private Date signTime;

    @Column(nullable = false, columnDefinition = "double default 0")
    private double totalCost = 0D;

    /**
     * 广告尺寸配置id
     */
    @Transient
    private String advertisementSizeConfigId;

    @Transient
    private String advertisementDurationConfigId;

    @Transient
    private String advertisementDisplayTimesConfigId;

    /**
     * 合同周期（天）
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer contractPeriod=0;

    /**
     * 合同已投放周期（天）
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer usedContractPeriod=0;

    /**
     * 详细的合同信息扩展懒加载
     */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "contract", fetch = FetchType.LAZY, optional = false)
    private ContractExtension contractExtension = new ContractExtension();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "contract", fetch = FetchType.LAZY, optional = false)
    private ContractAdvertisementConfig contractAdvertisementConfig = new ContractAdvertisementConfig();

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="contract")
    private List<Advertisement> advertisements;

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

    public ContractAdvertisementConfig getContractAdvertisementConfig() {
        return contractAdvertisementConfig;
    }

    public String getAdvertisementSizeConfigId() {
        return advertisementSizeConfigId;
    }

    public void setAdvertisementSizeConfigId(String advertisementSizeConfigId) {
        this.advertisementSizeConfigId = advertisementSizeConfigId;
    }

    public void setContractAdvertisementConfig(ContractAdvertisementConfig contractAdvertisementConfig) {
        this.contractAdvertisementConfig = contractAdvertisementConfig;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatusEnum = null;
        this.contractStatus = contractStatus;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public ContractExtension getContractExtension() {
        return contractExtension;
    }

    public void setContractExtension(ContractExtension contractExtension) {
        this.contractExtension = contractExtension;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOwnerId() {

        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
    }

    public User getSigner() {
        return signer;
    }

    public void setSigner(User signer) {
        this.signer = signer;
    }

    @Transient
    private ContractStatusEnum contractStatusEnum;

    public ContractStatusEnum getContractStatusEnum(){
        if(contractStatusEnum ==null){
            contractStatusEnum = EnumUtils.toEnum(getContractStatus(), ContractStatusEnum.class);
        }
        return contractStatusEnum;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Integer getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(Integer contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public Integer getUsedContractPeriod() {
        return usedContractPeriod;
    }

    public void setUsedContractPeriod(Integer usedContractPeriod) {
        this.usedContractPeriod = usedContractPeriod;
    }

    public void setContractStatusEnum(ContractStatusEnum contractStatusEnum) {
        this.contractStatusEnum = contractStatusEnum;
    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }
}
