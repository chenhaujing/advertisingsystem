package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.job.SyncStoreLog;
import com.sztouyun.advertisingsystem.service.job.StoreInfoServiceJob;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreInfoScheduledTask  extends BaseScheduledTask{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private StoreInfoServiceJob storeInfoServiceJob;
    @Value("${store.info.jobs.initdate}")
    private String initDate;

    //门店数据同步
    @Scheduled(cron = "${store.info.jobs.cron.day}")
    public void executeStoreInfoDayJob() {
        logger.info("门店数据同步day【开始】，当前时间：" + DateUtils.getCurrentFormat());

        if(!storeInfoServiceJob.isExist()){
            executeMonthList(initDate, DateUtils.getYesterdayFormat(Constant.DATA_YMD));
        }else{
            String startDate;
            String endDate;
            SyncStoreLog syncStoreLog = storeInfoServiceJob.findNewestLogBySuccess();
            if(syncStoreLog == null){
                startDate = initDate;
                endDate = DateUtils.getYesterdayFormat(Constant.DATA_YMD);
                executeMonthList(startDate, endDate);
            }else{
                startDate = syncStoreLog.getEndDate();//最近成功记录的结束日期
                endDate = DateUtils.getYesterdayFormat(Constant.DATA_YMD);
                storeInfoServiceJob.executeDay(startDate, endDate);
            }
        }

        logger.info("门店数据同步day【结束】，当前时间：" + DateUtils.getCurrentFormat());
    }

    private void executeMonthList(String startDate, String endDate) {
        List<String[]> monthList = DateUtils.getMonthArrayList(startDate, endDate);
        monthList.forEach(strArray->{
            storeInfoServiceJob.executeDay(strArray[0], strArray[1]);
        });
    }

    @Scheduled(cron = "${store.info.jobs.cron.month}")
    public void executeStoreInfoMonthJob() {
        logger.info("门店类型【month更新开始】，当前时间：" + DateUtils.getCurrentFormat());
        storeInfoServiceJob.executeMonth();
        logger.info("门店类型【month更新结束】，当前时间：" + DateUtils.getCurrentFormat());
    }
}