package com.sztouyun.advertisingsystem.viewmodel.account;

import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@ApiModel
public class CreateUserViewModel {

    @ApiModelProperty(value = "昵称", required = true)
    @NotBlank(message = "昵称不能为空")
    @Size(max = 128,message = "昵称太长")
    private String nickname;

    @ApiModelProperty(value = "电话号码", required = true)
    @NotBlank(message = "电话号码不能为空")
    @Pattern(regexp = Constant.REG_PHONE, message = "联系电话格式错误！")
    private String phoneNumber;

    @ApiModelProperty(value="角色ID",required = true)
    @NotBlank(message = "角色ID不能为空")
    private String roleId;

    @ApiModelProperty(value = "部门编号", required = false)
    @Size(max = 36,message = "部门编号太长")
    private String organizationId;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOrganizationId() {
        if(organizationId==null)
            this.organizationId="";
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
