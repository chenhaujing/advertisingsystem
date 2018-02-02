package com.sztouyun.advertisingsystem.service.customer;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.advertisement.material.QAdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.repository.account.UserRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.material.AdvertisementMaterialRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerAdvertisementStatistic;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerContractStatistic;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerPendingExecutionContractStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;


@Service
@CacheConfig(cacheNames = "customers")
public class CustomerService extends BaseService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AdvertisementMaterialRepository advertisementMaterialRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CodeGenerationService codeGenerationService;
    @Autowired
    private ContractRepository contractRepository;

    private final QCustomer qCustomer = QCustomer.customer;
    private final QContract qContract = QContract.contract;
    private final QAdvertisementMaterial qAdvertisementMaterial = QAdvertisementMaterial.advertisementMaterial;
    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;

    @Transactional
    public void createCustomer(Customer customer){
        int count = customerRepository.countByName(customer.getName());
        if(count>0)
            throw new BusinessException("客户名称已存在");
        String customerNumber = codeGenerationService.generateCode(CodeTypeEnum.BTE);
        customer.setCustomerNumber(customerNumber);
        User creator = getUser();
        customer.setOwnerId(creator.getId());
        customer.setCreatorId(creator.getId());
        customerRepository.save(customer);
    }

    @Transactional
    public void updateCustomer(Customer customer) {
        boolean isExist = customerRepository.exists(customer.getId());
        if(!isExist)
            throw new BusinessException("客户不存在");
        String isNameExist = customerRepository.findByNameAndId(customer.getId(),customer.getName());
        if(!StringUtils.isEmpty(isNameExist))
            throw new BusinessException("客户名称已存在");
        customer.setUpdatedTime(new Date());
        customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(String customerId ) {
        if(!customerRepository.exists(qCustomer.id.eq(customerId)))
            throw new BusinessException("该客户信息不存在");
        if (contractRepository.exists(qContract.customerId.eq(customerId)))
            throw new BusinessException("当前客户存在合同，无法直接删除");
        if(advertisementRepository.exists(qAdvertisement.customerId.eq(customerId)))
            throw new BusinessException("当前客户存在广告，无法直接删除");
        if(advertisementMaterialRepository.exists(qAdvertisementMaterial.customerId.eq(customerId)))
            throw new BusinessException("当前客户存在广告素材，无法直接删除");
        customerRepository.delete(customerId);
    }

    public Customer getCustomer(String id){
        if(StringUtils.isEmpty(id))
            throw new BusinessException("客户ID不能为空！");
        Customer customer= customerRepository.findOne(qCustomer.id.eq(id));
        if(null == customer)
            throw new BusinessException("客户不存在！");
        return customer;
    }

    public Page<Customer> queryCustomerList(String customerName,String areaId,Pageable pageable){
        BooleanBuilder predicate =new BooleanBuilder();
        if(!StringUtils.isEmpty(customerName)){
            predicate.and(qCustomer.name.contains(customerName));
        }
        if(!StringUtils.isEmpty(areaId)){
            predicate.and(qCustomer.provinceId.eq(areaId).or(qCustomer.cityId.eq(areaId).or(qCustomer.regionId.eq(areaId))));
        }
        return  customerRepository.findAllAuthorized(predicate,pageable);
    }

    @Transactional
    public void distributeCustomer(String id, String userId) {
        Customer customer = customerRepository.findOne(id);
        if(null == customer)
            throw new BusinessException("客户不存在");
        User user = userRepository.findById(userId);
        if(null == user)
            throw new BusinessException("业务员不存在");
        customer.setOwnerId(userId);
        customer.setUpdatedTime(new Date());
        contractRepository.updateOwnerIdByCustomerId(userId, id);//客户合同分配新的负责人
        customerRepository.save(customer);
    }

    public Page<CustomerPendingExecutionContractStatistic> getCustomerPendingExecutionContractStatistic(String customerName, Pageable pageable){
        BooleanBuilder filter = new BooleanBuilder();
        if(!StringUtils.isEmpty(customerName)){
            filter.and(qContract.customer.name.contains(customerName));
        }
        Page<CustomerPendingExecutionContractStatistic> list = contractRepository.findAllAuthorized(q->q
                .select(Projections.bean(CustomerPendingExecutionContractStatistic.class, qContract.customerId, qContract.id.count().as("pendingExecutionContractCount")))
                .from(qContract).innerJoin(qContract.customer)
                .on(qContract.contractStatus.eq(ContractStatusEnum.PendingExecution.getValue()))
                .where(filter)
                .groupBy(qContract.customerId),pageable);

        return  list;
    }

    @Cacheable(key = "#p0")
    public String getCustomerNameFromCache(String customerId) {
        return customerRepository.findOne(q->q.select(qCustomer.name).from(qCustomer).where(qCustomer.id.eq(customerId)));
    }

    public List<CustomerAdvertisementStatistic> getCustomerAdvertisementStatistics(List<String> customerIds){
        NumberExpression<Integer> deliveringAdvertising = qAdvertisement.advertisementStatus.when(AdvertisementStatusEnum.Delivering.getValue()).then(1).otherwise(0);
        return customerRepository.findAll(q ->q
                .select(Projections.bean(CustomerAdvertisementStatistic.class, qCustomer.id.as("customerId"),
                        qAdvertisement.id.countDistinct().as("advertisementDeliveryTimes"),
                        deliveringAdvertising.sum().as("advertisingDeliveringCount")))
                .from(qCustomer)
                .innerJoin(qCustomer.advertisements,qAdvertisement)
                .where(qCustomer.id.in(customerIds))
                .groupBy(qCustomer.id));
    }

    public List<CustomerContractStatistic> getCustomerContractStatistics(List<String> customerIds){
        return customerRepository.findAll(q ->q
                .select(Projections.bean(CustomerContractStatistic.class, qCustomer.id.as("customerId"),
                        qContract.totalCost.sum().as("contractTotalAmount")))
                .from(qCustomer)
                .innerJoin(qCustomer.contracts,qContract).on(qContract.contractStatus.eq(ContractStatusEnum.Finished.getValue()))
                .where(qCustomer.id.in(customerIds))
                .groupBy(qCustomer.id));
    }
}
