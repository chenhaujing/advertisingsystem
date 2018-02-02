package com.sztouyun.advertisingsystem.service.contract;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.*;
import com.sztouyun.advertisingsystem.model.contract.*;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementDisplayTimesConfigRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementDurationConfigRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementSizeConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractAdvertisementConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractExtensionRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractStoreRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractQueryRequest;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractStatusViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class ContractService extends BaseService {
    private final QCustomer qCustomer = QCustomer.customer;

    private final QContract qContract = QContract.contract;

    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    private final QContractStore qContractStore = QContractStore.contractStore;

    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;

    private final QContractAdvertisementConfig qContractAdvertisementConfig = QContractAdvertisementConfig.contractAdvertisementConfig;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContractStoreRepository contractStoreRepository;

    @Autowired
    private StoreInfoRepository storeInfoRepository;

    @Autowired
    private ContractExtensionRepository contractExtensionRepository;

    @Autowired
    private AdvertisementSizeConfigRepository advertisementSizeConfigRepository;

    @Autowired
    private AdvertisementDisplayTimesConfigRepository advertisementDisplayTimesConfigRepository;

    @Autowired
    private AdvertisementDurationConfigRepository advertisementDurationConfigRepository;

    @Autowired
    private ContractAdvertisementConfigRepository contractAdvertisementConfigRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Transactional
    public String createContract(Contract contract) {
        if (contract == null)
            throw new BusinessException("合同数据缺失");

        Boolean hasRepeatContractCode = contractRepository.exists(qContract.contractCode.eq(contract.getContractCode()).and(qContract.id.ne(contract.getId())));
        if (hasRepeatContractCode)
            throw new BusinessException("该合同编号已经存在");

        boolean hasRepeatContractName = contractRepository.exists(qContract.contractName.eq(contract.getContractName().trim()));
        if (hasRepeatContractName)
            throw new BusinessException("合同的名称已经存在");

        if (!customerRepository.exists(contract.getCustomerId()))
            throw new BusinessException("签约的客户不存在");

        contract.setCreatorId(getUser().getId());//设置负责人
        contract.setOwnerId(getUser().getId());//设置负责人

        ContractAdvertisementConfig contractAdvertisementConfig = createContractAdvertisementConfig(contract.getId(), contract.getAdvertisementSizeConfigId(), contract.getAdvertisementDurationConfigId(), contract.getAdvertisementDisplayTimesConfigId());
        contract.setContractAdvertisementConfig(contractAdvertisementConfig);
        contractAdvertisementConfig.setContract(contract);

        contract.setTotalCost(contract.getContractExtension().getTotalCost());
        contract.setContractStatus(ContractStatusEnum.PendingCommit.getValue());
        contractRepository.save(contract);
        return contract.getId();
    }

    public Contract getContract(String id) {
        Contract contract = contractRepository.findOneAuthorized(qContract.id.eq(id), new JoinDescriptor().leftJoin(qContract.contractExtension));
        if (null == contract)
            throw new BusinessException("合同不存在或者权限不足！");
        return contract;
    }

    /**
     * 根据合同id获取所有门店记录
     *
     * @param contactId
     * @return
     */
    public List<ContractStore> getContractStores(String contactId) {
        return contractStoreRepository.findByContractId(contactId);
    }

    @Transactional
    public void updateContract(Contract contract) {

        Integer contractStatus = contract.getContractStatus();
        if (!(contractStatus.equals(ContractStatusEnum.PendingCommit.getValue()) || contractStatus.equals(ContractStatusEnum.PendingAuditing.getValue())))
            throw new BusinessException("合同状态无效, 无法编辑");

        if (!customerRepository.exists(contract.getCustomerId()))
            throw new BusinessException("签约的客户不存在");

        boolean hasRepeatContractName = contractRepository.exists(qContract.contractName.eq(contract.getContractName().trim()).and(qContract.id.ne(contract.getId())));
        if (hasRepeatContractName)
            throw new BusinessException("合同名称已经存在");

        contract.setContractAdvertisementConfig(createContractAdvertisementConfig(contract.getId(), contract.getAdvertisementSizeConfigId(), contract.getAdvertisementDurationConfigId(), contract.getAdvertisementDisplayTimesConfigId()));
        contract.setContractStatus(ContractStatusEnum.PendingCommit.getValue());//保存之后设置为待提交状态
        contract.getContractExtension().setSaveTime(new Date());
        contract.setTotalCost(contract.getContractExtension().getTotalCost());
        contractRepository.save(contract);

    }

    @Transactional
    public InvokeResult deleteContract(String id) {
        Contract contract = contractRepository.findOne(id);
        if (contract == null)
            throw new BusinessException("查询不到合同相关信息");

        if (!ContractStatusEnum.PendingCommit.getValue().equals(contract.getContractStatus()))
            throw new BusinessException("合同状态无效, 无法删除");

        contractStoreRepository.deleteByContractId(id);
        contractExtensionRepository.delete(id);
        contractRepository.delete(id);
        return InvokeResult.SuccessResult();
    }

    public Page<Contract> getContractList(ContractQueryRequest queryRequest) {
        Pageable pageable = new MyPageRequest(queryRequest.getPageIndex(), queryRequest.getPageSize());
        BooleanBuilder predicate = new BooleanBuilder();
        if (!StringUtils.isEmpty(queryRequest.getContractName())) {
            predicate.and(qContract.contractName.contains(queryRequest.getContractName()));
        }
        if (!StringUtils.isEmpty(queryRequest.getContractCode())) {
            predicate.and(qContract.contractCode.contains(queryRequest.getContractCode()));
        }
        if (queryRequest.getContractStatus() > 0) {
            predicate.and(qContract.contractStatus.eq(queryRequest.getContractStatus()));
        }
        if(queryRequest.getCustomerIds() != null){
            predicate.and(qContract.customerId.in(queryRequest.getCustomerIds()));
        }
        return contractRepository.findAllAuthorized(predicate, pageable,queryRequest.getJoinDescriptor());
    }

    /**
     * 合同状态信息统计（合同总数、待审核、待签约、待执行）
     */
    public ContractStatusViewModel getContractStatusStatistics() {
        List<ContractStatusViewModel> contractStatusList = contractRepository.findAllAuthorized(queryFactory -> queryFactory
                .select(Projections.bean(ContractStatusViewModel.class, qContract.contractStatus.as("contractStatus"), qContract.count().as("statusCount")))
                .from(qContract)
                .groupBy(qContract.contractStatus));

        long total = 0;
        ContractStatusViewModel viewModel = new ContractStatusViewModel();
        for (ContractStatusViewModel contractStatusViewModel : contractStatusList) {
            if (ContractStatusEnum.PendingAuditing.getValue().equals(contractStatusViewModel.getContractStatus())) {
                viewModel.setToAudit(contractStatusViewModel.getStatusCount());
            }
            if (ContractStatusEnum.PendingSign.getValue().equals(contractStatusViewModel.getContractStatus())) {
                viewModel.setToSign(contractStatusViewModel.getStatusCount());
            }
            if (ContractStatusEnum.PendingExecution.getValue().equals(contractStatusViewModel.getContractStatus())) {
                viewModel.setToExecute(contractStatusViewModel.getStatusCount());
            }
            total += contractStatusViewModel.getStatusCount();
        }
        viewModel.setTotal(total);
        return viewModel;
    }

    @Transactional
    public void addContractStore(ContractStore contractStore) {
        QContractStore qContractStore = QContractStore.contractStore;
        Boolean isExists = contractStoreRepository.exists(
                qContractStore.contractId.eq(contractStore.getContractId())
                        .and(qContractStore.storeId.eq(contractStore.getStoreId()))
                        .and(qContractStore.storeType.eq(contractStore.getStoreType()))
        );
        if (isExists)
            throw new BusinessException("该记录已经存在");

        boolean isExistsStoreInfo = storeInfoRepository.exists(qStoreInfo.id.eq(contractStore.getStoreId()).and(qStoreInfo.storeType.eq(contractStore.getStoreType())));
        if (!isExistsStoreInfo)
            throw new BusinessException("门店数据不存在");

        ContractStore insertStore = contractStoreRepository.save(contractStore);
        if (insertStore == null)
            throw new BusinessException("插入门店数据失败");
    }

    public Long getTotalStoresByType(ContractStore contractStore) {
        return contractStoreRepository.count(qContractStore.contractId.eq(contractStore.getContractId()).and(qContractStore.storeType.eq(contractStore.getStoreType())));
    }

    @Transactional
    public void deleteContractStore(ContractStore contractStore) {
        contractStoreRepository.deleteByContractIdAndStoreId(contractStore.getContractId(), contractStore.getStoreId());

    }

    @Transactional
    public void deleteAllContractStores(String contractId, int storeType) {
        if (!contractRepository.exists(contractId))
            throw new BusinessException("查询不到合同数据");

        Boolean isExists = contractStoreRepository.exists(qContractStore.storeType.eq(storeType).and(qContractStore.contractId.eq(contractId)));
        if (!isExists) {
            throw new BusinessException("该数据已经被清空");
        }
        contractStoreRepository.deleteByContractIdAndStoreType(contractId, storeType);
    }

    public Page<Contract> getContractListByCustomerId(String customerId, Pageable pageable) {
        if (!customerRepository.existsAuthorized(qCustomer.id.eq(customerId)))
            throw new BusinessException("客户不存在或权限不足！");
        Page<Contract> pages = contractRepository.findAll(qContract.customerId.eq(customerId), pageable, new JoinDescriptor().innerJoin(qContract.contractExtension));
        return pages;
    }

    private ContractAdvertisementConfig createContractAdvertisementConfig(String contractId, String advertisementSizeConfigId, String advertisementDurationConfigId, String advertisementDisplayTimesConfigId) {
        ContractAdvertisementConfig newContractAdvertisementConfig = new ContractAdvertisementConfig();
        ContractAdvertisementConfig oldContractAdvertisementConfig = contractAdvertisementConfigRepository.findOne(qContractAdvertisementConfig.id.eq(contractId));
        if (oldContractAdvertisementConfig != null) {
            BeanUtils.copyProperties(oldContractAdvertisementConfig, newContractAdvertisementConfig);
        }

        if (!StringUtils.isEmpty(advertisementSizeConfigId)) {
            AdvertisementSizeConfig advertisementSizeConfig = advertisementSizeConfigRepository.findOne(advertisementSizeConfigId);
            if(advertisementSizeConfig == null)
                throw new BusinessException("广告尺寸配置不存在");
            BeanUtils.copyProperties(advertisementSizeConfig, newContractAdvertisementConfig);
            newContractAdvertisementConfig.setAdvertisementSizeConfigId(advertisementSizeConfigId);
        }
        if (!StringUtils.isEmpty(advertisementDisplayTimesConfigId)) {
            AdvertisementDisplayTimesConfig advertisementDisplayTimesConfig = advertisementDisplayTimesConfigRepository.findOne(advertisementDisplayTimesConfigId);
            if(advertisementDisplayTimesConfig == null)
                throw new BusinessException("广告展示次数配置不存在");
            BeanUtils.copyProperties(advertisementDisplayTimesConfig, newContractAdvertisementConfig);
            newContractAdvertisementConfig.setAdvertisementDisplayTimesConfigId(advertisementDisplayTimesConfigId);
        }
        if (!StringUtils.isEmpty(advertisementDurationConfigId)) {
            AdvertisementDurationConfig advertisementDurationConfig = advertisementDurationConfigRepository.findOne(advertisementDurationConfigId);
            if(advertisementDurationConfig == null)
                throw new BusinessException("广告时长配置不存在");
            BeanUtils.copyProperties(advertisementDurationConfig, newContractAdvertisementConfig);
            newContractAdvertisementConfig.setAdvertisementDurationConfigId(advertisementDurationConfigId);
        }

        newContractAdvertisementConfig.setId(contractId);//因为上面的BeanUtils.copyProperties会覆盖配置表的id, 这里重新设置会原来的id;
        return newContractAdvertisementConfig;
    }

    public Boolean hasUnDeliveryAdvertisement(String contractId) {
        return advertisementRepository.exists(
                qAdvertisement.contractId.eq(contractId)
                        .and(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.PendingCommit.getValue())
                                .or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.PendingAuditing.getValue()))
                                .or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.PendingDelivery.getValue()))
                                .or(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue()))
                        ));
    }
}
