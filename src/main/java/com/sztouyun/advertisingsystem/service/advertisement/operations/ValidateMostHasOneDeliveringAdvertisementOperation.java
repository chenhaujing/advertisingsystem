package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateMostHasOneDeliveringAdvertisementOperation implements IOperation<String> {
    @Autowired
    private AdvertisementRepository advertisementRepository;

    private final QAdvertisement qAdvertisement =QAdvertisement.advertisement;

    @Override
    public void operate(String contractId) {
        if(advertisementRepository.exists(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue())
                .and(qAdvertisement.contractId.eq(contractId))))
        throw new BusinessException("一个合同只能存在一个投放中状态的广告！");
    }
}
