package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.common.Constant;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class ContractExtension {

    @Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "contract"))
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn
    private Contract contract;

    /**
     * 甲方名称
     */
    @Column(nullable = false, length = 128)
    private String firstPartyName;

    /**
     * 甲方责任联系人
     */
    @Column(nullable = false, length = 128)
    private String firstPartyResponsibilityPerson;

    /**
     * 甲方合同接收地址
     */
    @Column(nullable = false, length = 128)
    private String firstPartyContractReceiveAddress;

    /**
     * 甲方邮件
     */
    @Column(nullable = false, length = 128)
    private String firstPartyEmail;

    /**
     * 甲方电话
     */
    @Column(nullable = false, length = 20)
    private String firstPartyPhone;

    /**
     * 乙方名称
     */
    @Column(nullable = false, length = 128)
    private String secondPartyName;

    /**
     * 乙方责任联系人
     */
    @Column(nullable = false, length = 128)
    private String secondPartyResponsibilityPerson;

    /**
     * 乙方合同接收地址
     */
    @Column(nullable = false, length = 128)
    private String secondPartyContractReceiveAddress;

    /**
     * 乙方邮件
     */
    @Column(nullable = false, length = 128)
    private String secondPartyEmail;

    /**
     * 乙方电话
     */
    @Column(nullable = false, length = 20)
    private String secondPartyPhone;

    /**
     * 媒体费用
     */
    @Column(nullable = false)
    private Double mediumCost;


    /**
     * 制作费用
     */
    @Column(nullable = false)
    private Double productCost;

    /**
     * 前述费用支付天数
     */
    @Column(nullable = false)
    private Integer signAfterDay;

    /**
     * 银行账户名称
     */
    @Column(nullable = false, length = 128)
    private String bankAccountName;

    /**
     * 银行账户号码
     */
    @Column(nullable = false, length = 30)
    private String bankAccountNumber;

    /**
     * 银行名称
     */
    @Column(nullable = false, length = 128)
    private String bankName;

    /**
     * 合作周期开始时间
     */
    @Column(nullable = false)
    private Date startTime;

    /**
     * 合作周期截止时间
     */
    @Column(nullable = false)
    private Date endTime;


    /**
     * 总月数
     */
    @Column(nullable = false)
    private Integer totalMonths;

    /**
     * 备注
     */
    @Column(length = 2000)
    private String remark;

    /**
     * A类门店数量
     */
    @Column
    private Integer storeACount;

    /**
     * B类门店
     */
    @Column
    private Integer storeBCount;

    /**
     * C类门店
     */
    @Column
    private Integer storeCCount;


    @Transient
    private Integer storeACountChecked;

    @Transient
    private Integer storeBCountChecked;

    @Transient
    private Integer storeCCountChecked;

    @Column
    private Date saveTime;

    @Column(nullable = false, columnDefinition = "double default 1")
    private Double discount;

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public Integer getStoreACountChecked() {
        return storeACountChecked;
    }

    public void setStoreACountChecked(Integer storeACountChecked) {
        this.storeACountChecked = storeACountChecked;
    }

    public Integer getStoreBCountChecked() {
        return storeBCountChecked;
    }

    public void setStoreBCountChecked(Integer storeBCountChecked) {
        this.storeBCountChecked = storeBCountChecked;
    }

    public Integer getStoreCCountChecked() {
        return storeCCountChecked;
    }

    public void setStoreCCountChecked(Integer storeCCountChecked) {
        this.storeCCountChecked = storeCCountChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
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

    /**
     * 获取费用总数
     * @return
     */
    public double getTotalCost(){
        BigDecimal mediumCostDecimal = BigDecimal.valueOf(this.mediumCost);
        BigDecimal productCostDecimal = BigDecimal.valueOf(this.productCost);
        BigDecimal discountDecimal = BigDecimal.valueOf(this.discount);
        return mediumCostDecimal.add(productCostDecimal).multiply(discountDecimal).setScale(Constant.SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取所有门店总数
     * @return
     */
    public Integer getTotalStoreCount() {
        return this.storeACount + this.storeBCount + this.storeCCount;
    }
}
