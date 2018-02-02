package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseModel;


import javax.persistence.*;

/**
 * Created by szty on 2017/7/27.
 */
@Entity
public class Permission extends BaseModel {

    @Column(nullable = false,length=128)
    private String permissionName;

    @Column(nullable = false,length=500)
    private String description;

    @Column(nullable = false,length=128)
    private String apiUrl;

    @Column(nullable = false,name = "menu_id",length = 36)
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
