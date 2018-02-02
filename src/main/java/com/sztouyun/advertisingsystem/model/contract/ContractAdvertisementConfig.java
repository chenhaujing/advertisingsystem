package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.common.DisplayTimeUnitEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ContractAdvertisementConfig {

    @Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @org.hibernate.annotations.Parameter(name = "property", value = "contract"))
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn
    private Contract contract;

    /**
     *展示次数
     */
    private Integer  displayTimes;

    /**
     *时间单位
     */
    private Integer timeUnit=1;

    /**
     *时长
     */
    private Integer duration;

    /**
     *时长单位
     */
    private Integer durationUnit;

    /**
     *尺寸名称
     */
    @Column(length = 50)
    private String sizeName;

    /**
     *水平分辨率
     */
    private Integer horizontalResolution;

    /**
     *垂直分辨率
     */
    private Integer verticalResolution;

    /**
     *宽度比
     */
    private Integer widthRatio;

    /**
     *高度比
     */
    private Integer highRatio;

    @Column(nullable = false, length = 36, columnDefinition = "varchar(36) default ''")
    private String advertisementDisplayTimesConfigId;

    @Column(nullable = false, length = 36, columnDefinition = "varchar(36) default ''")
    private String advertisementDurationConfigId;

    @Column(nullable = false, length = 36, columnDefinition = "varchar(36) default ''")
    private String advertisementSizeConfigId;

    public String getAdvertisementDisplayTimesConfigId() {
        return advertisementDisplayTimesConfigId;
    }

    public void setAdvertisementDisplayTimesConfigId(String advertisementDisplayTimesConfigId) {
        this.advertisementDisplayTimesConfigId = advertisementDisplayTimesConfigId;
    }

    public String getAdvertisementDurationConfigId() {
        return advertisementDurationConfigId;
    }

    public void setAdvertisementDurationConfigId(String advertisementDurationConfigId) {
        this.advertisementDurationConfigId = advertisementDurationConfigId;
    }

    public String getAdvertisementSizeConfigId() {
        return advertisementSizeConfigId;
    }

    public void setAdvertisementSizeConfigId(String advertisementSizeConfigId) {
        this.advertisementSizeConfigId = advertisementSizeConfigId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDisplayTimes() {
        return displayTimes;
    }

    public void setDisplayTimes(Integer displayTimes) {
        this.displayTimes = displayTimes;
    }

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(Integer durationUnit) {
        this.durationUnit = durationUnit;
    }

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

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getDisplayTimesInfo(){
        return this.getDisplayTimes()+"次/"+ EnumUtils.toValueMap(DisplayTimeUnitEnum.class).get(this.timeUnit);
    }
}
