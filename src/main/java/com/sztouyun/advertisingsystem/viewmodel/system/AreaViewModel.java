package com.sztouyun.advertisingsystem.viewmodel.system;

import com.sztouyun.advertisingsystem.common.ITreeList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by szty on 2017/7/27.
 */
@ApiModel
public class AreaViewModel  implements ITreeList<AreaViewModel>{
    @ApiModelProperty(value = "地区ID", required = true)
    private String id;
    @ApiModelProperty(value = "地区编号", required = true)
    private String code;
    @ApiModelProperty(value = "地区名称", required = true)
    private String name;
    @ApiModelProperty(value = "地区级别", required = true)
    private Integer level;
    @ApiModelProperty(value = "所属地区ID", required = true)
    private String parentId;
    private List<AreaViewModel> areaList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<AreaViewModel> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaViewModel> areaList) {
        this.areaList = areaList;
    }

    @Override
    public void setChildren(List<AreaViewModel> children) {
        setAreaList(children);
    }
}
