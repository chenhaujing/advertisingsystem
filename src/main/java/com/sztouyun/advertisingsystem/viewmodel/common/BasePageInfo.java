package com.sztouyun.advertisingsystem.viewmodel.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
public class BasePageInfo {
    @ApiModelProperty(value = "指定查询页码", required = true)
    @NotNull(message = "指定查询页码不能为空")
    @Min(value = 0,message = "查询页码不能小于0")
    protected Integer pageIndex;

    @ApiModelProperty(value = "分页大小", required = true)
    @NotNull(message = "分页大小不能为空")
    @Min(value = 1,message = "分页大小不能小于1")
    protected Integer pageSize;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
