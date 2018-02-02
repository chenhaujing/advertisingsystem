package com.sztouyun.advertisingsystem.service.store;


import com.sztouyun.advertisingsystem.model.AdvertisementAreaCountInfo;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStoreService {
    Page<StoreInfo> findStoreListByArea(String contractId, int storeType, String areaIds, Pageable pageable);

    int chooseStoreIdTop(String contractId, int storeType, String areaIds, int recordCount);

    int getChooseStoresCount(String contractId, int storeType);

    Page<AdvertisementAreaCountInfo> getAdvertisementAreaCountInfoList(String contractId, Pageable pageable);
}
