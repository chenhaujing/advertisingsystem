package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationEnum;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementOperationService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class AdvertisementScheduledTask extends BaseScheduledTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private AdvertisementOperationService advertisementOperationService;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;

    //定时检查待完成的广告
    @Scheduled(cron = "${advertisement.status.jobs.cron.minute}")
    @Transactional
    public void autoFinishAdvertisement() {
        setAdminLogin();

        List<Advertisement> list=advertisementRepository.findAllAdvertisementToFinish(AdvertisementStatusEnum.Delivering.getValue());
        logger.info("执行完成Job：自动完成广告和合同，暂无数据！");
        if(list.isEmpty())
            return;

        ScheduledJob scheduledJob=new ScheduledJob("自动完成广告和合同");
        StringBuffer remark=new StringBuffer();
        scheduledJob.setTaskSize(list.size());
        list.parallelStream().forEach(advertisement -> {
            try {
                advertisementOperationService.operate(getAdvertisementOperationLog(advertisement));
            }catch (Exception ex){
                scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
                remark.append(advertisement.getId()+" failed;");
                logger.error(ex.getMessage());
            }
        });
        remark.append("系统定时更新广告完成成功;");
        scheduledJob.setRemark(remark.toString());
        scheduledJobRepository.save(scheduledJob);
    }

    private AdvertisementOperationLog getAdvertisementOperationLog(Advertisement advertisement){
        AdvertisementOperationLog operationLog = new AdvertisementOperationLog();
        Integer operation= AdvertisementOperationEnum.Finish.getValue();
        Contract contract=advertisement.getContract();
        boolean successed =true;
        boolean finishContract = false;
        //是否完成合同
        Integer usedContractPeriod=contract.getUsedContractPeriod()+ DateUtils.getIntervalDays(advertisement.getEffectiveStartTime(),new Date());
        if(usedContractPeriod>=contract.getContractPeriod()){
            finishContract=true;
        }
        operationLog.setAdvertisementId(advertisement.getId());
        operationLog.setAdvertisement(advertisement);
        operationLog.setOperation(operation);
        operationLog.setSuccessed(successed);
        operationLog.setFinishContract(finishContract);
        return operationLog;
    }

}