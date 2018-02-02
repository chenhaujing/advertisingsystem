package com.sztouyun.advertisingsystem.viewmodel.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel
public class CreateCustomerViewModel {

    @ApiModelProperty(value = "客户名称", required = true)
    @Size(max = 128,message ="客户名称太长" )
    @NotBlank(message = "客户名称不能为空")
    private String name;

    @ApiModelProperty(value = "联系人", required = true)
    @Size(max = 128,message ="联系人太长" )
    @NotBlank(message = "联系人不能为空")
    private String contacts;

    @ApiModelProperty(value = "联系电话", required = true)
    @Pattern(regexp = "^[1][3,4,5,7,8][0-9]{9}$", message = "联系电话格式错误！")
    @Size(max = 20,message ="联系电话太长" )
    @NotBlank(message = "联系电话不能为空")
    private String contactNumber;

    @ApiModelProperty(value = "从属行业")
    @Size(max = 128,message ="从属行业太长" )
    private String industry;

    @ApiModelProperty(value = "从属子行业")
    @Size(max = 128,message ="从属子行业太长" )
    private String subIndustry;

    @ApiModelProperty(value = "邮箱")
    @Pattern(regexp = "^([\\w-_]+(?:\\.[\\w-_]+)*)@((?:[a-z0-9]+(?:-[a-zA-Z0-9]+)*)+\\.[a-z]{2,6})$||''", message = "邮箱格式错误！")
    @Size(max = 128,message ="邮箱太长" )
    private String mailAdress;

    @ApiModelProperty(value = "省份id")
    @Size(max = 128,message ="省份id太长" )
    private String provinceId;

    @ApiModelProperty(value = "城市id")
    @Size(max = 128,message ="城市id太长" )
    private String cityId;

    @ApiModelProperty(value = "区域id")
    @Size(max = 128,message ="区域id太长" )
    private String regionId;

    @ApiModelProperty(value = "具体地址")
    @Size(max = 128,message ="具体地址太长" )
    private String adressDetail;

    @ApiModelProperty(value = "备注")
    @Size(max = 2000,message ="备注太长" )
    private String remark;

    @ApiModelProperty(value = "头像")
    @Size(max = 128,message ="头像地址太长" )
    private String headPortrait;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
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
