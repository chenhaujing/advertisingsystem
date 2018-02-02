package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by szty on 2017/8/2.
 */
@ApiModel
public class ContractQueryRequest extends BasePageInfo {

    @ApiModelProperty(value = "合同名称")
    private String contractName;
    @ApiModelProperty(value = "合同编号")
    private String contractCode;
    @ApiModelProperty(value = "合同状态(0:全部 1:待提交 2:待审核 3:待签约 4:待执行 5:意外终止 6:执行完成)")
    private Integer contractStatus;
    @ApiModelProperty(value = "客户ID集合")
    private List<String> customerIds;
    @ApiModelProperty(hidden = true)
    private JoinDescriptor joinDescriptor;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public List<String> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<String> customerIds) {
        this.customerIds = customerIds;
    }

    public JoinDescriptor getJoinDescriptor() {
        return joinDescriptor;
    }

    public void setJoinDescriptor(JoinDescriptor joinDescriptor) {
        this.joinDescriptor = joinDescriptor;
    }
}
