package com.sztouyun.advertisingsystem.viewmodel.account;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by szty on 2017/8/3.
 */
public class UserInfoCountViewModel implements Serializable{

    @ApiModelProperty(value = "用户id")
    private String ownerId;

    @ApiModelProperty(value = "维护客户数量")
    private Long customerCount;

    @ApiModelProperty(value = "合同签约数量")
    private Long signedContractCount;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCustomerCount() {
        return customerCount ==null? 0:customerCount;
    }

    public void setCustomerCount(Long customerCount) {
        this.customerCount = customerCount;
    }

    public Long getSignedContractCount() {
        return signedContractCount;
    }

    public void setSignedContractCount(Long signedContractCount) {
        this.signedContractCount = signedContractCount;
    }

    public UserInfoCountViewModel() {
    }

    public UserInfoCountViewModel(String ownerId, Long customerCount, Long signedContractCount) {
        this.ownerId = ownerId;
        this.customerCount = customerCount;
        this.signedContractCount = signedContractCount;
    }
}
