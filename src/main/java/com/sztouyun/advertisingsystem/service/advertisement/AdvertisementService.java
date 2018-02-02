package com.sztouyun.advertisingsystem.service.advertisement;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.AdvertisementAreaCountInfo;
import com.sztouyun.advertisingsystem.model.advertisement.*;
import com.sztouyun.advertisingsystem.model.advertisement.material.AdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.advertisement.material.QAdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.common.DisplayTimeUnitEnum;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.*;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.material.AdvertisementMaterialRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractAdvertisementConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractExtensionRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.advertisement.material.AdvertisementMaterialService;
import com.sztouyun.advertisingsystem.service.contract.ContractAdvertisementConfigService;
import com.sztouyun.advertisingsystem.service.store.IStoreService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.*;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by wenfeng on 2017/8/4.
 */
@Service
public class AdvertisementService extends BaseService {
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractAdvertisementConfigRepository contractAdvertisementConfigRepository;
    @Autowired
    private ContractExtensionRepository contractExtensionRepository;
    @Autowired
    private AdvertisementMaterialService advertisementMaterialService;
    @Autowired
    private AdvertisementMaterialRepository advertisementMaterialRepository;
    @Autowired
    private IStoreService storeService;
    @Autowired
    private ContractAdvertisementConfigService contractAdvertisementConfigService;

    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;
    private final QContract qContract = QContract.contract;
    private final QAdvertisementMaterial qAdvertisementMaterial = QAdvertisementMaterial.advertisementMaterial;

    @Transactional
    public String createAdvertisement(Advertisement advertisement) {
        if (advertisementRepository.existsByAdvertisementName(advertisement.getAdvertisementName()))
            throw new BusinessException("广告名称不能重复，无法保存.");
        Contract contract = validateContractAndTime(advertisement);
        if (!contract.getContractStatus().equals(ContractStatusEnum.PendingExecution.getValue()))
            throw new BusinessException("合同不是待执行状态");
        if (advertisement.getAdvertisementType().equals(MaterialTypeEnum.Text.getValue()) && StringUtils.isEmpty(advertisement.getMaterialId())) {
            String materialId = advertisementMaterialService.saveTextMaterial(contract.getCustomerId(), advertisement.getData());
            advertisement.setMaterialId(materialId);
        }
        advertisement.setCustomerId(contract.getCustomerId());
        validateMaterial(advertisement);
        advertisement.setAdvertisementPeriod(DateUtils.getIntervalDays(advertisement.getStartTime(), advertisement.getEndTime()));
        advertisementRepository.save(advertisement);
        return advertisement.getId();
    }

    @Transactional
    public void deleteAdvertisement(String id) {
        Advertisement advertisement = advertisementRepository.findOne(id);
        if (advertisement == null)
            throw new BusinessException("广告不存在！");
        if (!advertisement.getAdvertisementStatus().equals(AdvertisementStatusEnum.PendingCommit.getValue()))
            throw new BusinessException("当前状态不允许删除！");
        advertisementRepository.delete(id);
    }

    @Transactional
    public void
    updateAdvertisement(Advertisement advertisement) {
        Advertisement oldAdvertisement = advertisementRepository.findOne(advertisement.getId());
        if (oldAdvertisement == null)
            throw new BusinessException("广告不存在！");
        if (advertisementRepository.existsByIdNotAndAdvertisementName(advertisement.getId(), advertisement.getAdvertisementName()))
            throw new BusinessException("广告名称不能重复，无法保存.");
        List<Integer> editableAdvertisementStatuses = Arrays.asList(AdvertisementStatusEnum.PendingCommit.getValue()
                , AdvertisementStatusEnum.PendingAuditing.getValue()
                , AdvertisementStatusEnum.PendingDelivery.getValue());
        if (!editableAdvertisementStatuses.contains(oldAdvertisement.getAdvertisementStatus()))
            throw new BusinessException("当前广告状态不允许编辑！");
        Contract contract = validateContractAndTime(advertisement);
        if (advertisement.getAdvertisementType().equals(MaterialTypeEnum.Text.getValue())) {
            String materialId = advertisementMaterialService.saveTextMaterial(contract.getCustomerId(), advertisement.getData());
            advertisement.setMaterialId(materialId);
        }
        validateMaterial(advertisement);
        advertisement.setUpdatedTime(new Date());
        advertisement.setAdvertisementStatus(AdvertisementStatusEnum.PendingCommit.getValue());
        advertisement.setAdvertisementPeriod(DateUtils.getIntervalDays(advertisement.getStartTime(), advertisement.getEndTime()));

        advertisementRepository.save(advertisement);
    }

    public Advertisement getAdvertisement(String id) {
        Advertisement advertisement = advertisementRepository.findOneAuthorized(qAdvertisement.id.eq(id), new JoinDescriptor().innerJoin(qAdvertisement.contract));
        if (null == advertisement)
            throw new BusinessException("广告不存在或权限不足！");
        return advertisement;
    }

    public Page<Advertisement> queryAdvertisementList(AdvertisementPageInfoViewModel viewModel) {
        if (viewModel.getHighRisk()) {
            viewModel.setAdvertisementStatus(AdvertisementStatusEnum.Delivering.getValue());
        }
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize(), new QSort(qAdvertisement.createdTime.desc()));
        BooleanBuilder predicate = new BooleanBuilder();
        if (!StringUtils.isEmpty(viewModel.getAdvertisementName())) {
            predicate = predicate.and(qAdvertisement.advertisementName.contains(viewModel.getAdvertisementName()));
        }
        if (!StringUtils.isEmpty(viewModel.getContractName())) {
            predicate = predicate.and(qAdvertisement.contract.contractName.contains(viewModel.getContractName()));
        }
        if (!StringUtils.isEmpty(viewModel.getCustomerName())) {
            predicate = predicate.and(qAdvertisement.customer.name.contains(viewModel.getCustomerName()));
        }
        if (viewModel.getAdvertisementStatus() > 0) {
            predicate = predicate.and(qAdvertisement.advertisementStatus.eq(viewModel.getAdvertisementStatus()));
        }
        if (viewModel.getHighRisk()) {
            predicate = predicate.and(qAdvertisement.expectedDueDay.lt(new Date()));
        }
        Page<Advertisement> pages = advertisementRepository.findAllAuthorized(predicate, pageable, viewModel.getJoinDescriptor());
        return pages;
    }

    /**
     * 广告状态信息统计（广告总数、待审核、待投放、投放中）
     */
    public AdvertisementStatusViewModel getAdvertisementStatusStatistics(AdvertisementStatusCountViewModel dto) {
        List<AdvertisementStatusViewModel> contractStatusList = advertisementRepository.findAllAuthorized(queryFactory -> {
            JPAQuery jpaQueryBase = queryFactory
                    .select(Projections.bean(AdvertisementStatusViewModel.class, qAdvertisement.advertisementStatus.as("advertisementStatus"), qAdvertisement.count().as("statusCount")))
                    .from(qAdvertisement);

            jpaQueryBase = filterQuery(jpaQueryBase, dto);
            jpaQueryBase.groupBy(qAdvertisement.advertisementStatus);
            return jpaQueryBase;
        });
        long total = 0;
        AdvertisementStatusViewModel viewModel = new AdvertisementStatusViewModel();
        for (AdvertisementStatusViewModel statusViewModel : contractStatusList) {
            if (statusViewModel.getAdvertisementStatus().equals(AdvertisementStatusEnum.PendingAuditing.getValue())) {
                viewModel.setPendingAuditing(statusViewModel.getStatusCount());
            }
            if (statusViewModel.getAdvertisementStatus().equals(AdvertisementStatusEnum.PendingDelivery.getValue())) {
                viewModel.setPendingDelivery(statusViewModel.getStatusCount());
            }
            if (statusViewModel.getAdvertisementStatus().equals(AdvertisementStatusEnum.Delivering.getValue())) {
                viewModel.setDeliverying(statusViewModel.getStatusCount());
            }
            total += statusViewModel.getStatusCount();
        }
        viewModel.setTotal(total);
        return viewModel;
    }


    public ContractAdvertisementConfigViewModel getContractAdvertisementConfig(String contractId) {
        ContractExtension contractExtension = contractExtensionRepository.findOne(contractId);
        if (contractExtension == null)
            throw new BusinessException("合同数据不存在");
        ContractAdvertisementConfig contractAdvertisementConfig = contractAdvertisementConfigRepository.findOne(contractId);
        if (contractAdvertisementConfig == null)
            throw new BusinessException("合同广告配置不存在");

        ContractAdvertisementConfigViewModel contractAdvertisementConfigViewModel = ApiBeanUtils.copyProperties(contractExtension, ContractAdvertisementConfigViewModel.class);
        BeanUtils.copyProperties(contractAdvertisementConfig, contractAdvertisementConfigViewModel);

        contractAdvertisementConfigViewModel.setTotalCost(contractExtension.getTotalCost());
        contractAdvertisementConfigViewModel.setTotalStores(contractExtension.getTotalStoreCount());
        contractAdvertisementConfigViewModel.setDurationUnitName(
                EnumUtils.toEnum(
                        contractAdvertisementConfig.getDurationUnit() == null ? DurationUnitEnum.S.getValue() : contractAdvertisementConfig.getDurationUnit(), DurationUnitEnum.class).getDisplayName());

        contractAdvertisementConfigViewModel.setTimeUnittName(
                EnumUtils.toEnum(
                        contractAdvertisementConfig.getTimeUnit() == null ? DisplayTimeUnitEnum.Day.getValue() : contractAdvertisementConfig.getTimeUnit(), DisplayTimeUnitEnum.class).getDisplayName());

        return contractAdvertisementConfigViewModel;

    }

    private Contract validateContractAndTime(Advertisement advertisement) {
        if (!advertisement.getStartTime().before(advertisement.getEndTime()))
            throw new BusinessException("广告投放结束时间必须大于开始时间");
        Contract contract = contractRepository.findOne(advertisement.getContractId());
        if (contract == null)
            throw new BusinessException("合同不存在");
        if (advertisement.getStartTime().before(contract.getContractExtension().getStartTime()))
            throw new BusinessException("广告投放开始时间，必须大于或等于合同投放周期开始时间");
        return contract;
    }

    private void validateMaterial(Advertisement advertisement) {
        if (StringUtils.isEmpty(advertisement.getMaterialId()))
            throw new BusinessException("投放不能为空，请上传或选择广告素材");
        AdvertisementMaterial advertisementMaterial = advertisementMaterialRepository.findOne(advertisement.getMaterialId());
        if (advertisementMaterial == null)
            throw new BusinessException("素材不存在");
        if (!advertisement.getAdvertisementType().equals(advertisementMaterial.getMaterialType()))
            throw new BusinessException("素材类型和广告类型不匹配");
        if (advertisementMaterial.getMaterialType().equals(MaterialTypeEnum.Video.getValue())) {
            if (FileUtils.getFileByteSize(advertisementMaterial.getMaterialSize()) > advertisementMaterialService.getVedioLimitSize())
                throw new BusinessException("所选视频素材超过大小限制");

        }
        if (advertisementMaterial.getMaterialType().equals(MaterialTypeEnum.Img.getValue())) {
            String configImgSpecification = advertisementMaterialService.getImgSpecification(advertisement.getContractId());
            String materailImgSpecification = advertisementMaterial.getMaterialSpecification();
            if (!materailImgSpecification.equals(configImgSpecification))
                throw new BusinessException("图片分辨率与当前配置不匹配");

        }

    }

    public Page<Advertisement> getAdvertisementListByCustomer(CustomerAdvertisementRequest dto) {
        Pageable pageable = new MyPageRequest(dto.getPageIndex(), dto.getPageSize(), new QSort(qAdvertisement.createdTime.desc()));
        BooleanBuilder predicate = new BooleanBuilder();
        predicate = predicate.and(qAdvertisement.customerId.eq(dto.getCustomerId()));
        Page<Advertisement> pages = advertisementRepository.findAll(predicate, pageable);
        return pages;
    }

    public Page<AdvertisementMaterial> getAdvertisementMaterialsByCustomer(Pageable pageable, String customerId) {
        return advertisementMaterialRepository.findAll(qAdvertisementMaterial.customerId.eq(customerId), pageable);
    }

    public List<AdvertisementStatusStatisticsViewModel> getAdvertisementDeliveringStatistics(AdvertisementStatusCountViewModel viewModel) {
        NumberExpression<Integer> deliveringAdvertising = qAdvertisement.advertisementStatus.when(AdvertisementStatusEnum.Delivering.getValue()).then(1).otherwise(0);
        NumberExpression<Integer> takeOffAdvertising = qAdvertisement.advertisementStatus.when(AdvertisementStatusEnum.TakeOff.getValue()).then(1).otherwise(0);
        NumberExpression<Integer> highRiskAdvertising = new CaseBuilder().when(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue()).and(qAdvertisement.expectedDueDay.lt(new Date()))).then(1).otherwise(0);

        return advertisementRepository.findAllAuthorized(queryFactor -> {
            JPAQuery jpaQuery = queryFactor.select(Projections.bean(AdvertisementStatusStatisticsViewModel.class,
                    deliveringAdvertising.sum().as("deliveringTotals"),
                    takeOffAdvertising.sum().as("takeOffTotals"),
                    highRiskAdvertising.sum().as("highRiskTotals")
            )).from(qAdvertisement);
            return filterQuery(jpaQuery, viewModel);
        });
    }

    public Page<AdvertisementAreaCountInfo> getAdvertisementReportInfo(String id, Pageable pageable) {
        Advertisement advertisement = advertisementRepository.findOne(id);
        if (advertisement == null)
            throw new BusinessException("广告ID无效");
        String contractId = advertisement.getContractId();
        if (StringUtils.isEmpty(contractId))
            throw new BusinessException("广告ID无效");
        Page<AdvertisementAreaCountInfo> page = storeService.getAdvertisementAreaCountInfoList(contractId, pageable);
        return page;
    }


    public ContractAdvertisementDeliveryStatistics getContractAdvertisementDeliveryStatistics(Contract contract, int advertisementPeriod) {

        if(!hasAdvertisement(contract.getId()) && contract.getContractPeriod() >= advertisementPeriod)
            return null;

        ContractAdvertisementDeliveryStatistics contractAdvertisementDeliveryStatistics = new ContractAdvertisementDeliveryStatistics();

        int effectivePeriod = contract.getContractPeriod() - contract.getUsedContractPeriod();

        Advertisement deliveringAdvertisement = advertisementRepository.findOne(qAdvertisement.contractId.eq(contract.getId()).and(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue())));
        if (deliveringAdvertisement != null) {
            int intervalDays = DateUtils.getIntervalDays(deliveringAdvertisement.getEffectiveStartTime(), new Date());
            effectivePeriod = effectivePeriod - intervalDays;
        }

        long hasAdvertisedCount = advertisementRepository.count(
                qAdvertisement.contractId.eq(contract.getId()).and(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.TakeOff.getValue()).or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Finished.getValue()))));

        contractAdvertisementDeliveryStatistics.setHasAdvertisedTimes(hasAdvertisedCount);
        contractAdvertisementDeliveryStatistics.setEffectivePeriod(effectivePeriod);

        return contractAdvertisementDeliveryStatistics;
    }

    public Boolean hasAdvertisement(String contractId) {
        return advertisementRepository.exists(
                qAdvertisement.contractId.eq(contractId).and(
                        qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue())
                                .or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.TakeOff.getValue()))
                                .or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Finished.getValue()))));
    }

     public Date getCustomerLastestDeliveryTime(String customerId){
        if(StringUtils.isEmpty(customerId))
            return null;

         return advertisementRepository.findOne(q->q
                 .select(qAdvertisement.effectiveStartTime).from(qAdvertisement)
                 .where(qAdvertisement.customerId.eq(customerId)).orderBy(qAdvertisement.effectiveStartTime.desc()));
     }


    /**
     * 根据广告名称,或者合同名称, 或者客户名称进行查询
     *
     * @param jpaQuery
     * @param viewModel
     * @return
     */
    private JPAQuery filterQuery(JPAQuery jpaQuery, AdvertisementStatusCountViewModel viewModel) {
        if (!StringUtils.isEmpty(viewModel.getAdvertisementName())) {
            jpaQuery.where(qAdvertisement.advertisementName.contains(viewModel.getAdvertisementName()));
        } else if (!StringUtils.isEmpty(viewModel.getContractName())) {
            jpaQuery.innerJoin(qAdvertisement.contract).where(qAdvertisement.contract.contractName.contains(viewModel.getContractName()));
        } else if (!StringUtils.isEmpty(viewModel.getCustomerName())) {
            jpaQuery.innerJoin(qAdvertisement.customer).where(qAdvertisement.customer.name.contains(viewModel.getCustomerName()));
        }
        return jpaQuery;
    }

}
