package com.sztouyun.advertisingsystem.service.advertisement;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementSizeConfig;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisementSizeConfig;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementSizeConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AdvertisementSizeConfigService extends BaseService {

    @Autowired
    private AdvertisementSizeConfigRepository sizeConfigRepository;

    private final QAdvertisementSizeConfig qAdvertisementSizeConfig = QAdvertisementSizeConfig.advertisementSizeConfig;

    @Transactional
    public void createSizeConfig(AdvertisementSizeConfig sizeConfig) {
        boolean exists = sizeConfigRepository.exists(
                qAdvertisementSizeConfig.sizeName.eq(sizeConfig.getSizeName()));

        if(exists)
            throw new BusinessException("已经存在对应的尺寸配置");

        sizeConfigRepository.save(sizeConfig);
    }

    @Transactional
    public void updateSizeConfig(AdvertisementSizeConfig sizeConfig) {
        boolean exists = sizeConfigRepository.exists(sizeConfig.getId());
        if(!exists)
            throw new BusinessException("广告尺寸配置数据不存在");

        boolean hasRepeat = sizeConfigRepository.exists(
                qAdvertisementSizeConfig.sizeName.eq(sizeConfig.getSizeName()).and(qAdvertisementSizeConfig.id.ne(sizeConfig.getId())));

        if(hasRepeat)
            throw new BusinessException("已经存在相对的广告尺寸配置");

        sizeConfigRepository.save(sizeConfig);
    }

    public AdvertisementSizeConfig getSizeConfig(String id) {
        boolean exists = sizeConfigRepository.exists(id);
        if(!exists)
            throw new BusinessException("广告尺寸配置数据不存在");

        return sizeConfigRepository.findOne(qAdvertisementSizeConfig.id.eq(id));
    }

    public void deleteSizeConfig(String id) {
        boolean exists = sizeConfigRepository.exists(id);
        if(!exists)
            throw new BusinessException("广告尺寸配置数据不存在");

        sizeConfigRepository.delete(id);
    }

    public Page<AdvertisementSizeConfig> getAllSizeConfitList(Pageable pageable) {
        return sizeConfigRepository.findAll(pageable);
    }

}

