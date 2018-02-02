package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@ApiModel
public class StoreInfoPageInfoViewModel extends BasePageInfo {

    @ApiModelProperty(value = "合同Id", required = true)
    @NotEmpty(message = "合同Id不能为空")
    private String contractId;
    @ApiModelProperty(value = "门店类型", required = true)
    @NotNull(message = "门店类型不能为空")
    private Integer storeType;
    @ApiModelProperty(value = "地区ID 字符串", required = true)
    @NotEmpty(message = "地区ID不能为空")
    private String areaIds;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public String getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }
}
