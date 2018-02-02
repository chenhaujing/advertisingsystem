package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
public class AutoChooseStoreInfoViewModel {
    @ApiModelProperty(value = "合同Id", required = true)
    @NotEmpty(message = "合同Id不能为空")
    private String contractId;
    @ApiModelProperty(value = "门店类型", required = true)
    @NotNull(message = "门店类型不能为空")
    private Integer storeType;
    @ApiModelProperty(value = "地区ID 字符串(以英文逗号间隔)", required = true)
    @NotEmpty(message = "地区ID不能为空")
    private String areaIds;
    @ApiModelProperty(value = "自动选择记录数", required = true)
    @Min(value=1,message = "自动选择记录数不能小于0")
    @NotNull(message = "自动选择记录数不能为空")
    private Integer recordCount;

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

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }
}
