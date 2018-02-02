package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.model.contract.ContractStatusEnum;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidateContractStatusOperation implements IOperation<ContractOperationLog>{
    @Autowired
    private ContractRepository contractRepository;

    private Map<ContractStatusEnum, List<ContractOperationEnum>> contractStatusMapping = new HashMap<ContractStatusEnum, List<ContractOperationEnum>>() {
        {
            put(ContractStatusEnum.PendingCommit, Arrays.asList(ContractOperationEnum.Submit));
            put(ContractStatusEnum.PendingAuditing,  Arrays.asList(ContractOperationEnum.Auditing));
            put(ContractStatusEnum.PendingSign,  Arrays.asList(ContractOperationEnum.Sign));
            put(ContractStatusEnum.PendingExecution,  Arrays.asList(ContractOperationEnum.BeginExecute,ContractOperationEnum.Finish));
            put(ContractStatusEnum.Executing,  Arrays.asList(ContractOperationEnum.StageFinish,ContractOperationEnum.Finish));
        }
    };

    @Override
    public void operate(ContractOperationLog contractOperationLog) {
        Contract contract =contractOperationLog.getContract();
        if(contract == null){
            contract = contractRepository.findOne(contractOperationLog.getContractId());
        }
        List<ContractOperationEnum> targetOperationEnums = contractStatusMapping.get(contract.getContractStatusEnum());
        if (!targetOperationEnums.contains(contractOperationLog.getContractOperationEnum()))
            throw new BusinessException("当前合同状态不支持本操作！");
    }
}
