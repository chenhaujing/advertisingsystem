package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IOperation;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementOperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveAdvertisementOperationLogOperation implements IOperation<AdvertisementOperationLog> {
    @Autowired
    private AdvertisementOperationLogRepository advertisementOperationLogRepository;


    @Override
    public void operate(AdvertisementOperationLog advertisementOperationLog) {
        advertisementOperationLogRepository.save(advertisementOperationLog);
    }
}
