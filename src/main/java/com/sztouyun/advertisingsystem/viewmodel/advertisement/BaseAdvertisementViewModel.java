package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by wenfeng on 2017/8/4.
 */
@ApiModel
public class BaseAdvertisementViewModel {
    @ApiModelProperty(value = "广告名称)")
    @NotBlank(message = "请输入广告名称")
    private String advertisementName;


    @ApiModelProperty(value = "素材内容)")
    private String data;
    @ApiModelProperty(value = "素材ID)")
    private String materialId;

    @ApiModelProperty(value = "广告类型(图片:1，文本:2 ,视频:3)", required = true)
    @NotNull(message = "广告类型不能为空")
    @EnumValue(enumClass = MaterialTypeEnum.class,message = "广告类型不匹配")
    private Integer advertisementType;


    @ApiModelProperty(value = "广告投放开始时间", required = true)
    @JsonFormat(pattern = Constant.DATETIME,timezone = "GMT+8")
    @NotNull(message = "请选择有效的广告投放开始时间")
    @Future(message = "广告开始投放时间必须大于当前时间(精确到小时)")
    private Date startTime;

    @ApiModelProperty(value = "广告投放截止时间", required = true)
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    @NotNull(message = "请选择有效的广告投放结束时间")
    @Future(message = "广告投放结束时间必须大于当前时间(精确到小时)")
    private Date endTime;

    @ApiModelProperty(value = "备注")
    private String  remark;

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        this.advertisementName = advertisementName;
    }

    public Integer getAdvertisementType() {
        return advertisementType;
    }

    public void setAdvertisementType(Integer advertisementType) {
        this.advertisementType = advertisementType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
