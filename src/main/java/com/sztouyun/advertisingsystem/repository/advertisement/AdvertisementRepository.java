package com.sztouyun.advertisingsystem.repository.advertisement;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AdvertisementRepository extends BaseRepository<Advertisement> {
    boolean existsByAdvertisementName(String id);

    boolean existsByIdNotAndAdvertisementName(String id,String advertisementName);

    @Override
    default BooleanBuilder getAuthorizationFilter() {
        return AuthenticationService.getUserAuthenticationFilter(QAdvertisement.advertisement.contract.ownerId);
    }

    List<Advertisement> findAllByAdvertisementStatusAndExpectedDueDayBefore(Integer status, Date date);

    @Query("select advertisement from  Advertisement advertisement where advertisement.advertisementStatus=?1 and  DATE_ADD(advertisement.effectiveStartTime,advertisement.advertisementPeriod) <= NOW()")
    List<Advertisement> findAllAdvertisementToFinish(Integer status);
}
