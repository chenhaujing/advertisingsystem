package com.sztouyun.advertisingsystem.model.advertisement;

public class ContractAdvertisementDeliveryStatistics {

    /**
     * 已投放次数
     */
    private Long hasAdvertisedTimes = 0L;

    /**
     * 有效周期
     */
    private Integer effectivePeriod = 0;

    public Long getHasAdvertisedTimes() {
        return hasAdvertisedTimes;
    }

    public void setHasAdvertisedTimes(Long hasAdvertisedTimes) {
        this.hasAdvertisedTimes = hasAdvertisedTimes;
    }

    public Integer getEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(Integer effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }
}
