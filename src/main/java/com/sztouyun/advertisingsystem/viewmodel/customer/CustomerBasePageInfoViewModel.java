package com.sztouyun.advertisingsystem.viewmodel.customer;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by szty on 2017/7/27.
 */
@ApiModel
public class CustomerBasePageInfoViewModel extends BasePageInfo {

    @ApiModelProperty(value = "查询客户列表的用户name")
    private String customername;

    @ApiModelProperty(value = "查询客户列表地区id")
    private String areaId;

    public String getCustomername() {
        if(customername ==null)
        {
            customername ="";
        }
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
