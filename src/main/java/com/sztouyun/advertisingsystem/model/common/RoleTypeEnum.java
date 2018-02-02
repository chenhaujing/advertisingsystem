package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum RoleTypeEnum implements EnumMessage<Integer> {
    Admin(99,"系统管理员"),
    ManagerialStaff(2,"管理人员"),
    SaleMan(3,"业务员");


    private Integer value;
    private String displayName;

    RoleTypeEnum(Integer value,String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
