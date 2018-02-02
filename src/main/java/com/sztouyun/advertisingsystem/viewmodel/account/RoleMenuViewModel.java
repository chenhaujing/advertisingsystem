package com.sztouyun.advertisingsystem.viewmodel.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by szty on 2017/8/1.
 */
@ApiModel
public class RoleMenuViewModel {
    @ApiModelProperty(value = "角色ID", required = true)
    @NotEmpty(message = "角色ID不能为空")
    private String roleId;

    @ApiModelProperty(value = "菜单ID集合(包括上级菜单ID在内)", required = true)
    @NotEmpty(message = "菜单ID不能为空")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
