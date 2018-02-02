package com.sztouyun.advertisingsystem.service.advertisement;

import com.sztouyun.advertisingsystem.common.operation.BaseOperationService;
import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationEnum;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementOperationLogRepository;
import com.sztouyun.advertisingsystem.service.advertisement.operations.*;
import com.sztouyun.advertisingsystem.service.common.operations.ValidateAuditingPermissionOperation;
import com.sztouyun.advertisingsystem.service.contract.operations.ContractActionOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementOperationService extends BaseOperationService<AdvertisementOperationLog> {
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private AdvertisementOperationLogRepository advertisementOperationLogRepository;

    @Override
    protected void onOperating(AdvertisementOperationLog advertisementOperationLog, IOperationCollection<AdvertisementOperationLog> operationCollection) {
        if(advertisementOperationLog.getAdvertisementStatusEnum() == null)
            throw new BusinessException("操作类型错误!");

        Advertisement advertisement = advertisementService.getAdvertisement(advertisementOperationLog.getAdvertisementId());
        advertisementOperationLog.setAdvertisement(advertisement);
        operationCollection.add(ValidateAdvertisementStatusOperation.class);
        switch (advertisementOperationLog.getAdvertisementOperationEnum()){
            case Auditing:
                operationCollection.add(ValidateAuditingPermissionOperation.class);
                break;
            case Delivery:
                operationCollection.add(ValidateMostHasOneDeliveringAdvertisementOperation.class,advertisement.getContractId());
                break;
        }
        operationCollection.add(SaveAdvertisementOperationLogOperation.class);
        operationCollection.add(UpdateAdvertisementStatusOperation.class);
    }

    @Override
    protected void onOperated(AdvertisementOperationLog advertisementOperationLog, IOperationCollection<AdvertisementOperationLog> operationCollection) {
        Advertisement advertisement = advertisementOperationLog.getAdvertisement();
        switch (advertisementOperationLog.getAdvertisementOperationEnum()){
            case Delivery:
                operationCollection.add(UpdateDelivererAndDeliveryTimeOperation.class,advertisement);
                //投放后开始执行合同
                String remark ="投放广告"+advertisement.getAdvertisementName();
                ContractOperationLog contractOperationLog = new ContractOperationLog(advertisement.getContractId(), ContractOperationEnum.BeginExecute.getValue(),true,remark);
                operationCollection.add(ContractActionOperation.class,contractOperationLog);
                break;
            case Finish:
                operationCollection.add(UpdateEffectiveEndTimeAndPeriodOperation.class,advertisement);
                //完成投放后阶段完成合同
                remark = advertisementOperationLog.isSuccessed() ?"投放完成":"下架"+advertisement.getAdvertisementName();
                contractOperationLog = new ContractOperationLog(advertisement.getContractId(), ContractOperationEnum.StageFinish.getValue(),true,remark);
                operationCollection.add(ContractActionOperation.class,contractOperationLog);

                if(advertisementOperationLog.isFinishContract()){
                    remark = advertisementOperationLog.isSuccessed()?"达到合同周期自动完成合同":"下架并完成合同";
                    contractOperationLog= new ContractOperationLog(advertisement.getContractId(), ContractOperationEnum.Finish.getValue(),true,remark);
                    operationCollection.add(ContractActionOperation.class,contractOperationLog);
                }
                break;

        }
    }

    public AdvertisementOperationLog findTakeOffLog(String advertisementID){
        return advertisementOperationLogRepository.findByAdvertisementIdAndOperationAndSuccessed(advertisementID, AdvertisementOperationEnum.Finish.getValue(),false);
    }
}
