package com.sztouyun.advertisingsystem.service.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztouyun.advertisingsystem.model.contract.StoreCategoryStandard;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.job.SyncStoreLog;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.repository.contract.StoreCategoryStandardRepository;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.repository.job.SyncStoreLogRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.viewmodel.job.ResponseStoreInfoViewModel;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class StoreInfoServiceJob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${store.info.url}")
    private String getUrl;
    @Value("${store.info.header.key}")
    private String headerKey;
    @Value("${store.info.header.value}")
    private String headerValue;

    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    @Autowired
    private StoreCategoryStandardRepository storeCategoryStandardRepository;
    @Autowired
    private SyncStoreLogRepository syncStoreLogRepository;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;

    @Transactional
    public void executeDay(String startDate, String endDate) {
        List<ResponseStoreInfoViewModel> responseList = getHttpEntity(startDate, endDate);
        List<Area> areas = areaService.getAllAreasFromCache();

        List<StoreInfo> storeInfoList = new ArrayList<>();//此处应该有String转List<StoreInfo>对象
        responseList.forEach(responseStoreInfoViewModel -> {
            StoreInfo storeInfo = new StoreInfo();
            storeInfo.setStoreNo(responseStoreInfoViewModel.getId());//id
            storeInfo.setStoreName(responseStoreInfoViewModel.getName());//name
            storeInfo.setProvinceId(responseStoreInfoViewModel.getProvince() == null ? null : getAreaIdByCode(areas, responseStoreInfoViewModel.getProvince().getCode()));//province.code
            storeInfo.setCityId(responseStoreInfoViewModel.getCity() == null ? null : getAreaIdByCode(areas, responseStoreInfoViewModel.getCity().getCode()));//city.code
            storeInfo.setRegionId(responseStoreInfoViewModel.getDistrict() == null ? null : getAreaIdByCode(areas, responseStoreInfoViewModel.getDistrict().getCode()));//district.code
            storeInfo.setEquipmentCode(responseStoreInfoViewModel.getDeviceId());//deviceId-设备编码
            storeInfo.setStoreAddress(responseStoreInfoViewModel.getAddress());//address
            storeInfo.setCreatedAt(responseStoreInfoViewModel.getCreatedAt());//createdAt-开店时间
            storeInfo.setCommodityCount(responseStoreInfoViewModel.getCommodityNumber());//commodityNumber
            storeInfo.setTransactionCount(responseStoreInfoViewModel.getOrderNumber());//orderNumber
            storeInfoList.add(storeInfo);
        });

        int total = synchroniseStoreInfoList(storeInfoList);

        saveRecordLog(startDate, endDate, total, total > 0 ? true : false);
    }

    /**
     * 数据库中已有门店信息，更新门店类型
     */
    @Transactional
    public void executeMonth() {
        ScheduledJob scheduledJob=new ScheduledJob("门店类型更新");
        List<StoreInfo> allList = storeInfoRepository.findAll();
        List<StoreInfo> updateList = calculateStoreType(allList);
        scheduledJob.setTaskSize(updateList.size());
        logger.info("更新门店类型数据.size：" + updateList.size());
        storeInfoRepository.save(updateList);
        scheduledJob.updateSuccessed();
        scheduledJobRepository.save(scheduledJob);
    }

    /**
     * 数据表是否有记录
     *
     * @return
     */
    public boolean isExist() {
        int count = (int) syncStoreLogRepository.count();
        return count > 0;
    }

    /**
     * 获取最新的成功记录
     *
     * @return
     */
    public SyncStoreLog findNewestLogBySuccess() {
        return syncStoreLogRepository.findFirstByIsSuccessOrderByCreatedTimeDesc(1);
    }

    /**
     * 根据地区code，查找地区ID
     *
     * @param areas
     * @param code
     * @return
     */
    private String getAreaIdByCode(List<Area> areas, String code) {
        if (StringUtils.isEmpty(code))
            return "";
        Area area = Linq4j.asEnumerable(areas).firstOrDefault(a -> a.getCode().equals(code));
        if (area == null)
            return "";
        return area.getId();
    }

    /**
     * 获取请求接口返回数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private List<ResponseStoreInfoViewModel> getHttpEntity(String startDate, String endDate) {
        List<ResponseStoreInfoViewModel> responseList = new ArrayList<>();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(headerKey, headerValue);
            HttpEntity<Object> entity = new HttpEntity<Object>(headers);

            Map<String, Object> uriVariables = new HashMap<String, Object>();
            uriVariables.put("startDate", startDate);
            uriVariables.put("endDate", endDate);
            uriVariables.put("t", new Date().getTime());
            getUrl = getUrl + "?startDate={startDate}&endDate={endDate}&t={t}";
            ResponseEntity<String> responseEntity = restTemplate.exchange(getUrl, HttpMethod.GET, entity, String.class, uriVariables);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if (responseEntity.getBody() != null) {
                responseList = mapper.readValue(responseEntity.getBody(), new TypeReference<List<ResponseStoreInfoViewModel>>() {
                });
            }
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("门店信息接口调用异常.startDate:" + startDate + ",endDate:" + endDate, e);
        }
        return responseList;
    }

    private int synchroniseStoreInfoList(List<StoreInfo> storeList) {
        int total = storeList.size();
        List<String> newStoreNos = Linq4j.asEnumerable(storeList).select(l -> l.getStoreNo()).toList();
        Enumerable<StoreInfo> existStoreList = Linq4j.asEnumerable(storeInfoRepository.findAll(QStoreInfo.storeInfo.storeNo.in(newStoreNos)));
        List<StoreInfo> newStoreInfoList =new ArrayList<>();
        storeList.forEach(storeInfo -> {
             StoreInfo existStoreInfo = existStoreList.firstOrDefault(s ->s.getStoreNo().equals(storeInfo.getStoreNo()));
             if(existStoreInfo != null){
                 existStoreInfo.setEquipmentCode(storeInfo.getEquipmentCode());
                 existStoreInfo.setCommodityCount(storeInfo.getCommodityCount());
                 existStoreInfo.setTransactionCount(storeInfo.getTransactionCount());
            }else {
                 newStoreInfoList.add(storeInfo);
             }
        });
        storeInfoRepository.save(existStoreList);
        storeInfoRepository.save(newStoreInfoList);
        logger.info("同步门店数据.size：" + total);
        return total;
    }

    /**
     * 动态计算门店类型
     *
     * @param storeList
     * @return
     */
    private List<StoreInfo> calculateStoreType(List<StoreInfo> storeList) {
        List<StoreCategoryStandard> standardList = storeCategoryStandardRepository.findAll();
        storeList.forEach(storeInfo -> storeInfo.setStoreType(getStoreType(storeInfo, standardList)));
        return storeList;
    }

    private int getStoreType(StoreInfo storeInfo, List<StoreCategoryStandard> standardList) {
        int days = DateUtils.getIntervalDays(storeInfo.getCreatedAt(), new Date());
        int type = 0;
        if (days > 0) {
            Double avgAmount = Double.parseDouble(storeInfo.getTransactionCount() + "") / Double.parseDouble(days + "");
            type = getTypeByRule(standardList, storeInfo.getCommodityCount(), avgAmount);
        }
        return type;
    }

    /**
     * 根据门店类别划分标准，获取门店类型
     *
     * @param standardList
     * @param commodityAmount
     * @param AvgDailyTradingAmount
     * @return
     */
    private int getTypeByRule(List<StoreCategoryStandard> standardList, int commodityAmount, Double AvgDailyTradingAmount) {
        for (StoreCategoryStandard storeCategoryStandard : standardList) {
            if (commodityAmount >= storeCategoryStandard.getCommodityAmountMin()
                    && commodityAmount < storeCategoryStandard.getCommodityAmountMax()
                    && AvgDailyTradingAmount >= storeCategoryStandard.getAvgDailyTradingAmountMin()
                    && AvgDailyTradingAmount < storeCategoryStandard.getAvgDailyTradingAmountMax())
                return storeCategoryStandard.getStoreType();
        }
        return 0;
    }

    /**
     * 同步门店数据日志 入库
     *
     * @param startDate
     * @param endDate
     * @param total
     * @param isSuccess
     */
    private void saveRecordLog(String startDate, String endDate, int total, boolean isSuccess) {
        SyncStoreLog syncStoreLog = new SyncStoreLog();
        syncStoreLog.setStartDate(startDate);
        syncStoreLog.setEndDate(endDate);
        syncStoreLog.setRecordCount(total);
        syncStoreLog.setIsSuccess(isSuccess ? 1 : 0);

        syncStoreLogRepository.save(syncStoreLog);
    }
}