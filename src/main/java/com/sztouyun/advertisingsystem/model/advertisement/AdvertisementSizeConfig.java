package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AdvertisementSizeConfig extends BaseModel {

    /**
     * 尺寸名称
     */
    @Column(nullable = false, length = 50)
    private String sizeName;

    /**
     * 水平分辨率
     */
    @Column(nullable = false)
    private Integer horizontalResolution;

    /**
     * 垂直分辨率
     */
    @Column(nullable = false)
    private Integer verticalResolution;

    /**
     * 宽度比
     */
    @Column(nullable = false)
    private Integer widthRatio;

    /**
     * 高度比
     */
    @Column(nullable = false)
    private Integer highRatio;

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public Integer getHorizontalResolution() {
        return horizontalResolution;
    }

    public void setHorizontalResolution(Integer horizontalResolution) {
        this.horizontalResolution = horizontalResolution;
    }

    public Integer getVerticalResolution() {
        return verticalResolution;
    }

    public void setVerticalResolution(Integer verticalResolution) {
        this.verticalResolution = verticalResolution;
    }

    public Integer getWidthRatio() {
        return widthRatio;
    }

    public void setWidthRatio(Integer widthRatio) {
        this.widthRatio = widthRatio;
    }

    public Integer getHighRatio() {
        return highRatio;
    }

    public void setHighRatio(Integer highRatio) {
        this.highRatio = highRatio;
    }
}
