package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IOperation;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.repository.contract.ContractOperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveContractOperationLogOperation implements IOperation<ContractOperationLog> {
    @Autowired
    private ContractOperationLogRepository contractOperationLogRepository;

    @Override
    public void operate(ContractOperationLog contractOperationLog) {
        contractOperationLogRepository.save(contractOperationLog);
    }
}
