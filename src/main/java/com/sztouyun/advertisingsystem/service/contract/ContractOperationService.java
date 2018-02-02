package com.sztouyun.advertisingsystem.service.contract;

import com.sztouyun.advertisingsystem.common.operation.BaseOperationService;
import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.service.common.operations.ValidateAuditingPermissionOperation;
import com.sztouyun.advertisingsystem.service.contract.operations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractOperationService extends BaseOperationService<ContractOperationLog> {
    @Autowired
    private ContractService contractService;

    @Override
    protected void onOperating(ContractOperationLog contractOperationLog, IOperationCollection<ContractOperationLog> operationCollection) {
        if(contractOperationLog.getContractOperationEnum() == null)
            throw new BusinessException("操作类型错误！");

        Contract contract = contractService.getContract(contractOperationLog.getContractId());
        contractOperationLog.setContract(contract);
        operationCollection.add(ValidateContractStatusOperation.class);
        switch (contractOperationLog.getContractOperationEnum()){
            case Submit:
                operationCollection.add(ValidateContractStoreCountOperation.class,contractOperationLog.getContract());
                break;
            case Auditing:
                operationCollection.add(ValidateAuditingPermissionOperation.class);
                break;
        }
        operationCollection.add(SaveContractOperationLogOperation.class);
        operationCollection.add(UpdateContractStatusOperation.class);
    }

    @Override
    protected void onOperated(ContractOperationLog contractOperationLog, IOperationCollection<ContractOperationLog> operationCollection) {
        Contract contract = contractOperationLog.getContract();
        switch (contractOperationLog.getContractOperationEnum()){
            case Sign:
                if(contractOperationLog.isSuccessed()){
                    operationCollection.add(UpdateSignerAndSignTimeOperation.class,contractOperationLog.getContract());
                }
                break;
            case StageFinish:
                operationCollection.add(UpdateUsedContractPeriodOperation.class,contract);
                break;
            case Finish:
                //下架合同广告
                operationCollection.add(TakeOffContractAdvertisementOperation.class,contract);
                if(!contractOperationLog.isSuccessed()){
                    operationCollection.add(UpdateUsedContractPeriodOperation.class,contract);
                }
                break;
        }
    }
}
