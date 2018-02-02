package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by wenfeng on 2017/8/4.
 */
@ApiModel
public class CreateAdvertisementViewModel extends  BaseAdvertisementViewModel{

    @ApiModelProperty(value = "合同ID)")
    @NotBlank(message = "合同ID不允许为空")
    private String contractId;


    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

}
