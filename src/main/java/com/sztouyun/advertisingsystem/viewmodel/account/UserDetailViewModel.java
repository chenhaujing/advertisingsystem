package com.sztouyun.advertisingsystem.viewmodel.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class UserDetailViewModel extends UpdateUserViewModel {

    @ApiModelProperty(value = "用户是否启用", required = true)
    @NotNull(message = "启用标记不能为空")
    private boolean enableFlag;

    @ApiModelProperty(value = "创建者名称", required = true)
    private String creator="";

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createdTime;

    @ApiModelProperty(value= "角色类型",required = true)
    private String roleType;

    @ApiModelProperty(value="所属组织名称")
    private String organizationName;

    @ApiModelProperty(value="上级领导Id")
    private String leaderId;

    @ApiModelProperty(value="上级领导")
    private String leader;

    public boolean isEnable() {
        return enableFlag;
    }

	
    public void setEnableFlag(boolean enableFlag) {
        this.enableFlag = enableFlag;
    }
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }
}
