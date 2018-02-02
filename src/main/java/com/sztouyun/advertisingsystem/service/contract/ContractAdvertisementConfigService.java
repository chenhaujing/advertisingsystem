package com.sztouyun.advertisingsystem.service.contract;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.ContractAdvertisementConfig;
import com.sztouyun.advertisingsystem.repository.contract.ContractAdvertisementConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractAdvertisementConfigService extends BaseService {

    @Autowired
    private ContractAdvertisementConfigRepository contractAdvertisementConfigRepository;

    public ContractAdvertisementConfig getContractAdvertisementConfig(String id) {

        ContractAdvertisementConfig contractAdvertisementConfig = contractAdvertisementConfigRepository.findOne(id);
        if(contractAdvertisementConfig == null)
            throw new BusinessException("合同广告配置不存在");

        return contractAdvertisementConfig;
    }

}
