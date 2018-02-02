package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.utils.EnumUtils;

import javax.persistence.*;

@Entity
public class ContractOperationLog extends BaseModel {
    public ContractOperationLog(){}

    public ContractOperationLog(String contractId, Integer operation, boolean successed, String remark) {
        this.contractId = contractId;
        this.operation = operation;
        this.successed = successed;
        this.remark = remark;
    }

    /**
     * 对应合同id
     */
    @Column(name = "contract_id",nullable = false, length = 36)
    private String contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id",insertable = false,updatable = false)
    private Contract contract;

    /**
     * 操作
     */
    @Column(nullable = false)
    private Integer operation;

    /**
     * 是否成功
     */
    @Column(nullable = false)
    private boolean successed;

    /**
     * 备注
     */
    @Column(length = 2000)
    private String remark;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.contractOperationEnum = null;
        this.operation = operation;
    }

    public boolean isSuccessed() {
        return successed;
    }

    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Transient
    private ContractOperationEnum contractOperationEnum;

    public ContractOperationEnum getContractOperationEnum(){
        if(contractOperationEnum == null){
            contractOperationEnum = EnumUtils.toEnum(getOperation(), ContractOperationEnum.class);
        }
        return contractOperationEnum;
    }
}
