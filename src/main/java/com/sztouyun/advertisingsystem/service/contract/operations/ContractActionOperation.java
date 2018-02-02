package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IOperation;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.service.contract.ContractOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractActionOperation implements IOperation<ContractOperationLog> {
    @Autowired
    private ContractOperationService operationService;

    @Override
    public void operate(ContractOperationLog contractOperationLog) {
        operationService.operate(contractOperationLog);
    }
}
