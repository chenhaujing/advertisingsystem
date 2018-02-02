package com.sztouyun.advertisingsystem.service.store;

import com.qiniu.util.StringUtils;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.AdvertisementAreaCountInfo;
import com.sztouyun.advertisingsystem.model.contract.ContractStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractStore;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.contract.QContractStore;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractStoreRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreServiceImpl extends BaseService implements IStoreService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ContractStoreRepository contractStoreRepository;

    private final QContract qContract = QContract.contract;

    private final QContractStore qContractStore = QContractStore.contractStore;

    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    @Override
    public Page<StoreInfo> findStoreListByArea(String contractId, int storeType, String areaIds, Pageable pageable) {
        if(!contractRepository.exists(qContract.id.eq(contractId)))
            throw new BusinessException("合同Id不存在");

        Query query = getResultQuery(contractId, storeType, areaIds);
        Query queryTotals = getResultQueryCount(contractId, storeType, areaIds);

        int totals = Integer.parseInt(queryTotals.getSingleResult().toString());

        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        Page<StoreInfo> pageRst = new PageImpl<StoreInfo>(query.getResultList(), pageable, totals);
        return pageRst;
    }

    /**
     * 获取门店
     * @param contractId
     * @param storeType
     * @param areaIds
     * @return
     */
    public Query getResultQuery(String contractId, int storeType, String areaIds){
        Query query = null;
        try{
            String sqlStr = "select * from ("+ getQuerySql(contractId, storeType, areaIds) +") a ORDER BY a.isChoose DESC, a.surplusCount DESC, a.city_id DESC";
            query = createNativeQuery(sqlStr, Constant.STOREINFO_SELECT);
        }catch (Exception e){
            logger.error("获取门店信息sql语句出错.", e);
        }

        return query;
    }

    public Query getResultQueryCount(String contractId, int storeType, String areaIds) {
        Query query = null;
        try {
            String sqlStr = "select count(*) from (" + getQuerySql(contractId, storeType, areaIds) +") a";
            query = createNativeQuery(sqlStr);

        } catch (Exception e) {
            logger.error("获取门店信息sql语句出错.", e);
        }
        return query;
    }



    @Override
    @Transactional
    public int chooseStoreIdTop(String contractId, int storeType, String areaIds, int recordCount) {
        if(!contractRepository.exists(qContract.id.eq(contractId)))
            throw new BusinessException("合同Id不存在");

        int selectedCount = (int)contractStoreRepository.count(qContractStore.contractId.eq(contractId).and(qContractStore.storeType.eq(storeType)));//已经选中保存在数据库中的

        int chooseCount = recordCount - selectedCount;//待选择的数量
        if(chooseCount < 0) {
            contractStoreRepository.deleteByContractIdAndStoreType(contractId, storeType);
            chooseCount = recordCount;
        }

        Query query = getResultQuery(contractId, storeType, areaIds);
        List<StoreInfo> storeInfoList = query.getResultList();
        int curTotal = 0;
        if(storeInfoList != null && storeInfoList.size() > 0){
            List<ContractStore> contractStoreList = new ArrayList<>();
            ContractStore contractStore;
            for (StoreInfo storeInfo : storeInfoList){
                if(curTotal >= chooseCount)
                    break;
                if(storeInfo.getIsChoose() == 1)//当前合同中，若门店已经选择，则本次一键选取就不添加此门店
                    continue;

                curTotal++;
                contractStore = new ContractStore();
                contractStore.setContractId(contractId);
                contractStore.setStoreId(storeInfo.getId());
                contractStore.setStoreType(storeType);
                contractStoreList.add(contractStore);
            }
            /**
             *  执行批量插入，合同子表 contractStroe
             */
            contractStoreRepository.save(contractStoreList);
        }

        return curTotal;
    }

    @Override
    public int getChooseStoresCount(String contractId, int storeType) {
        return (int)contractStoreRepository.count(qContractStore.contractId.eq(contractId).and(qContractStore.storeType.eq(storeType)));
    }

    private String getQuerySql(String contractId, int storeType, String areaIds) {
        String[] areaIdArray = areaIds.split(",");
        String areaIdStr = "'" + StringUtils.join(areaIdArray, "','") + "'";

        return " SELECT m.id,m.store_no,m.store_name,m.equipment_code,m.store_address,n.name AS city_id," +
                " m.usedCount,m.isChoose,m.surplusCount FROM (" +
                " SELECT t1.id,t1.store_no,t1.store_name,t1.store_type,t1.equipment_code,t1.store_address,t1.city_id," +
                " IFNULL(t1.ad_count,0)-IFNULL(t2.num,0) AS surplusCount,IFNULL(t2.num,0) AS usedCount,(case WHEN t3.contract_id is NULL THEN 0 ELSE 1 END) AS isChoose FROM (" +
                "     SELECT a.*,b.ad_count FROM store_info a LEFT JOIN ad_position b ON a.store_type = b.store_type " +
                "     WHERE a.store_type = "+storeType+" AND a.city_id in ("+areaIdStr+") " +
                " ) t1 LEFT JOIN ( " +
                "    SELECT d.store_id, COUNT(d.id) AS num FROM contract c LEFT JOIN contract_store d ON c.id = d.contract_id " +
                "    WHERE c.contract_status < "+ ContractStatusEnum.AbruptlyTerminated.getValue() +
                "    AND d.store_type= "+storeType+" GROUP BY d.store_id " +
                " ) t2 ON t1.id = t2.store_id LEFT JOIN " +
                "contract_store  t3 on t3.contract_id = '"+contractId+"' AND t3.store_type = "+storeType+" and t1.id = t3.store_id" +
                " ) m LEFT JOIN AREA n ON m.city_id = n.id " +
                " WHERE m.surplusCount > 0";
    }

    public Page<AdvertisementAreaCountInfo> getAdvertisementAreaCountInfoList(String contractId,Pageable pageable){
        Page<AdvertisementAreaCountInfo> page=contractStoreRepository.findAll(q->q
                .select(Projections.bean(AdvertisementAreaCountInfo.class, qContractStore.storeInfo.cityId.as("cityId"), qContractStore.storeId.count().as("storeCount")))
                .from(qContractStore).leftJoin(qContractStore.storeInfo)
                .where(qContractStore.contractId.eq(contractId))
                .groupBy(qContractStore.storeInfo.cityId),pageable);
        return page;
    }

}
