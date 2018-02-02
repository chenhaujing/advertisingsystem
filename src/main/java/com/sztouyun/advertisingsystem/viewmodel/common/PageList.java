package com.sztouyun.advertisingsystem.viewmodel.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class PageList<T> extends BasePageInfo {
    @ApiModelProperty(value = "记录总数", required = true)
    private Long totalElement;

    @ApiModelProperty(value = "总页数", required = true)
    private Integer totalPageSize;

    @ApiModelProperty(value = "数据列表", required = true)
    private List<T> list;

    @ApiModelProperty(value = "列表已选择记录数")
    private Integer chooseCount;

    public PageList() {
    }

    public Long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(Long totalElement) {
        this.totalElement = totalElement;
    }

    public Integer getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(Integer totalPageSize) {
        this.totalPageSize = totalPageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getChooseCount() {
        return chooseCount;
    }

    public void setChooseCount(Integer chooseCount) {
        this.chooseCount = chooseCount;
    }
}
