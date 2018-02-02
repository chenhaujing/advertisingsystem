package com.sztouyun.advertisingsystem.viewmodel.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * Created by szty on 2017/7/28.
 */
@ApiModel
public class CreatePermissionViewModel {

    @ApiModelProperty(value = "权限名称",required = true)
    @NotEmpty(message = "权限名称不能为空")
    private String permissionName;

    @ApiModelProperty(value = "权限描述")
    private String description;

    @ApiModelProperty(value = "权限路径",required = true)
    @NotEmpty(message = "权限路径不能为空")
    private String apiUrl;

    @ApiModelProperty(value = "菜单Id",required = true)
    @NotEmpty(message = "菜单Id不能为空")
    private String menuId;


    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
