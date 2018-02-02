package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.UpdateAdvertisementSizeConfigViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class UpdateContractViewModel extends BaseContractViewModel {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "已经选择的A类门店数量")
    private Integer storeACountChecked;

    @ApiModelProperty(value = "已经选择的B类门店数量")
    private Integer storeBCountChecked;

    @ApiModelProperty(value = "已经选择的C类门店数量")
    private Integer storeCCountChecked;

    public Integer getStoreACountChecked() {
        return storeACountChecked;
    }

    public void setStoreACountChecked(Integer storeACountChecked) {
        this.storeACountChecked = storeACountChecked;
    }

    public Integer getStoreBCountChecked() {
        return storeBCountChecked;
    }

    public void setStoreBCountChecked(Integer storeBCountChecked) {
        this.storeBCountChecked = storeBCountChecked;
    }

    public Integer getStoreCCountChecked() {
        return storeCCountChecked;
    }

    public void setStoreCCountChecked(Integer storeCCountChecked) {
        this.storeCCountChecked = storeCCountChecked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
