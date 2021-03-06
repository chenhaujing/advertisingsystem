package com.sztouyun.advertisingsystem.viewmodel.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class CreateRoleViewModel {
    @ApiModelProperty(value = "角色名", required = true)
    @NotBlank(message = "角色名不能为空")
    @Size(max = 128,message = "角色名太长")
    private String roleName;

    @ApiModelProperty(value = "角色描述信息", required = true)
    @Size(max = 500,message = "角色描述信息太长")
    private String description;

    @ApiModelProperty(value = "角色类型", required = true)
    @Min(value = 1,message = "角色类型不存在")
    @Max(value = 3,message = "角色类型不存在")
    @NotNull(message = "角色类型不能为空")
    private Integer roleType;

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        if(description==null)
            return "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }





}
