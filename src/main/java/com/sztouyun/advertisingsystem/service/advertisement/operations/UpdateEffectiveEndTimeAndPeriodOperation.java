package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IOperation;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UpdateEffectiveEndTimeAndPeriodOperation implements IOperation<Advertisement> {
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Override
    public void operate(Advertisement advertisement) {
        //更新实际结束时间、实际广告周期
        if(advertisement.getEffectiveStartTime() == null)
            return;
        Date effectiveEndTime = DateUtils.getDateAccurateToMinute(new Date());
        advertisement.setEffectiveEndTime(effectiveEndTime);
        advertisement.setEffectivePeriod(DateUtils.getIntervalDays(advertisement.getEffectiveStartTime(),effectiveEndTime));
        advertisementRepository.save(advertisement);
    }
}
