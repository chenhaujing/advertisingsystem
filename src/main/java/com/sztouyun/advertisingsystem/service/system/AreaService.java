package com.sztouyun.advertisingsystem.service.system;

import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.model.system.QArea;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.system.AreaRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by szty on 2017/7/25.
 */
@Service
@CacheConfig(cacheNames = "areas")
public class AreaService extends BaseService {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final QArea qArea = QArea.area;

    private final QCustomer qCustomer = QCustomer.customer;

    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    @Cacheable
    public List<Area> getAllAreasFromCache() {
        return areaRepository.findAll();
    }

    public List<Area> getExistsCustomerAreas() {
        return customerRepository.findAllAuthorized(queryFactory -> queryFactory.selectDistinct(qArea)
                .from(qCustomer, qArea).where(qCustomer.cityId.eq(qArea.id)
                        .or(qCustomer.provinceId.eq(qArea.id)).or(qCustomer.regionId.eq(qArea.id))));
    }

    public List<Area> getAreaByContainStoreInfo() {
        return areaRepository.findAll(queryFactory -> queryFactory.selectDistinct(qArea)
                .from(qStoreInfo, qArea).where(qStoreInfo.cityId.eq(qArea.id
                ).or(qStoreInfo.provinceId.eq(qArea.id))));
    }

    @Cacheable(key = "#p0")
    public String getAreaNameFromCache(String areaId) {
        if (StringUtils.isEmpty(areaId))
            return "";
        Area area = Linq4j.asEnumerable(getAllAreasFromCache()).firstOrDefault(a -> a.getId().equals(areaId));
        if (area == null)
            return "";
        return area.getName();
    }

    public String getParentAreaNameFromCache(String areaId){
        if (StringUtils.isEmpty(areaId))
            return "";
        Area area = Linq4j.asEnumerable(getAllAreasFromCache()).firstOrDefault(a -> a.getId().equals(areaId));
        if (area == null)
            return "";
        return getAreaNameFromCache(area.getParentId());
    }
}
