package com.sztouyun.advertisingsystem.model.customer;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.contract.Contract;

import javax.persistence.*;
import java.util.List;

/**
 * Created by szty on 2017/7/25.
 */
@Entity
public class Customer extends BaseModel {
    /**
     *客户名称
     */
    @Column(nullable = false,length=128)
    private String name;
    /**
     * 客户编号
     */
    @Column(length=128)
    private String customerNumber;
    /**
     * 联系人
     */
    @Column(nullable = false,length=128)
    private String contacts;
    /**
     * 联系电话
     */
    @Column(nullable = false,length=20)
    private String contactNumber;
    /**
     * 从属行业
     */
    @Column(length=128)
    private String industry;
    /**
     * 从属子行业
     */
    @Column(length=128)
    private String subIndustry;
    /**
     * 邮箱
     */
    @Column(length=128)
    private String mailAdress;

    /**
     * 省份id
     */
    @Column(length=36,name = "province_id")
    private String provinceId;

    /**
     * 城市id
     */
    @Column(length=36,name = "city_id")
    private String cityId;

    /**
     * 区域id
     */
    @Column(length=36,name = "region_id")
    private String regionId;

    /**
     * 详细地址
     */
    @Column(length=128)
    private String adressDetail;

    /**
     * 备注
     */
    @Column(length=2000)
    private String remark;
    /**
     * 头像
     */
    @Column(length=128)
    private String headPortrait;
    /**
     * 所属用户Id
     */
    @Column(length=36,name = "owner_id")
    private String ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false,updatable = false)
    private User owner;

    @OneToMany(fetch=FetchType.LAZY,mappedBy="customer")
    private List<Contract> contracts;

    @OneToMany(fetch=FetchType.LAZY,mappedBy="customer")
    private List<Advertisement> advertisements;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSubIndustry() {
        return subIndustry;
    }

    public void setSubIndustry(String subIndustry) {
        this.subIndustry = subIndustry;
    }

    public String getMailAdress() {
        return mailAdress;
    }

    public void setMailAdress(String mailAdress) {
        this.mailAdress = mailAdress;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAdressDetail() {
        return adressDetail;
    }

    public void setAdressDetail(String adressDetail) {
        this.adressDetail = adressDetail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }
}
