package com.sztouyun.advertisingsystem.repository.advertisement;

import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementPackageConfig;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementPriceConfig;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface AdvertisementPackageConfigRepository extends BaseRepository<AdvertisementPackageConfig> {
    boolean exists(String id);

    boolean existsByPackageNameAndActived(String PackageName, Boolean isActive);

    boolean existsByIdNotAndPackageNameAndActived(String id, String PackageName, Boolean isActive);

    Page<AdvertisementPackageConfig> findAllByPackageNameContainingAndActived( String packageName, Boolean isActive,Pageable pageable);


}
