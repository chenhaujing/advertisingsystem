package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.*;
import com.sztouyun.advertisingsystem.repository.contract.ContractStoreRepository;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidateContractStoreCountOperation implements IOperation<Contract> {
    @Autowired
    private ContractStoreRepository contractStoreRepository;

    @Override
    public void  operate(Contract contract) {
        List<ContractStore> contractStoreList = contractStoreRepository.findByContractId(contract.getId());
        Enumerable<ContractStore> contractStores = Linq4j.asEnumerable(contractStoreList);
        ContractExtension contractExtension = contract.getContractExtension();
        if (contractExtension == null)
            throw new BusinessException("合同信息不完整！");

        if (contractExtension.getStoreACount() == 0 && contractExtension.getStoreBCount() == 0 && contractExtension.getStoreCCount() == 0)
            throw new BusinessException("请最少选择一家门店！");

        if (contractExtension.getStoreACount() != contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.A.getValue()))
                || contractExtension.getStoreBCount() != contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.B.getValue()))
                || contractExtension.getStoreCCount() != contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.C.getValue())))
            throw new BusinessException("请先完成门店选择！");
    }
}
