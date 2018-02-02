package com.sztouyun.advertisingsystem.api.contract;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.model.advertisement.DurationUnitEnum;
import com.sztouyun.advertisingsystem.model.common.DisplayTimeUnitEnum;
import com.sztouyun.advertisingsystem.model.contract.*;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementPriceConfigService;
import com.sztouyun.advertisingsystem.service.contract.ContractAdvertisementConfigService;
import com.sztouyun.advertisingsystem.service.contract.ContractOperationService;
import com.sztouyun.advertisingsystem.service.contract.ContractService;
import com.sztouyun.advertisingsystem.service.contract.PreviewService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.contract.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Api(value = "合同管理接口")
@RestController
@RequestMapping("/api/contract")
public class ContractApiController extends BaseApiController {

    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractOperationService contractOperationService;
    @Autowired
    private PreviewService previewService;
    @Autowired
    private AdvertisementPriceConfigService advertisementPriceConfigService;

    @Autowired
    private ContractAdvertisementConfigService contractAdvertisementConfigService;

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "所有合同状态", notes = "创建者：李川")
    @RequestMapping(value = "statuses", method = RequestMethod.GET)
    public InvokeResult<Map<Integer,String>> queryContractStatusList() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return InvokeResult.SuccessResult(EnumUtils.toValueMap(ContractStatusEnum.class));
    }

    @ApiOperation(value = "创建合同", notes = "创建人: 王伟权")
    @PostMapping(value = "create")
    public InvokeResult<String> createContract(@Validated @RequestBody CreateContractViewModel viewModel, BindingResult result) {

        if (result.hasErrors())
            return ValidateFailResult(result);

        Contract contract = new Contract();
        ContractExtension contractExtension = new ContractExtension();
        ContractAdvertisementConfig contractAdvertisementConfig = new ContractAdvertisementConfig();

        BeanUtils.copyProperties(viewModel, contractExtension);
        BeanUtils.copyProperties(viewModel, contract);

        contractExtension.setContract(contract);
        contract.setContractPeriod(DateUtils.getIntervalDays(contractExtension.getStartTime(),contractExtension.getEndTime()));
        contract.setContractExtension(contractExtension);

        contractAdvertisementConfig.setContract(contract);
        contract.setContractAdvertisementConfig(contractAdvertisementConfig);

        contractService.createContract(contract);
        return InvokeResult.SuccessResult(contract.getId());
    }


    @ApiOperation(value = "编辑合同获取已保存的数据", notes = "创建人: 王伟权")
    @GetMapping(value = "/{id}/editInfo")
    public InvokeResult<UpdateContractDetailViewModel> getContractToUpdate(@PathVariable String id) {

        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("合同id不能为空");

        Contract contract = contractService.getContract(id);

        List<ContractStore> contractStoreList = contractService.getContractStores(id);

        ContractAdvertisementConfig contractAdvertisementConfig = contractAdvertisementConfigService.getContractAdvertisementConfig(id);

        Enumerable<ContractStore> contractStores = Linq4j.asEnumerable(contractStoreList);
        contract.getContractExtension().setStoreACountChecked(contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.A.getValue())));
        contract.getContractExtension().setStoreBCountChecked(contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.B.getValue())));
        contract.getContractExtension().setStoreCCountChecked(contractStores.count(c -> c.getStoreType().equals(StoreTypeEnum.C.getValue())));

        InvokeResult<UpdateContractDetailViewModel> updateInvokeResult = new InvokeResult<UpdateContractDetailViewModel>();//最终返回结果
        UpdateContractDetailViewModel updateContractDetailViewModel = new UpdateContractDetailViewModel();
        BeanUtils.copyProperties(contract, updateContractDetailViewModel);
        BeanUtils.copyProperties(contract.getContractExtension(), updateContractDetailViewModel);
        BeanUtils.copyProperties(contractAdvertisementConfig, updateContractDetailViewModel);
        updateInvokeResult.setData(updateContractDetailViewModel);
        return updateInvokeResult;
    }

    @ApiOperation(value = "根据合同id删除合同数据", notes = "创建人: 王伟权")
    @PostMapping(value = "/{id}/delete")
    public InvokeResult deleteContract(@PathVariable String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("缺失请求参数");
        return contractService.deleteContract(id);
    }

    @ApiOperation(value = "更新合同数据", notes = "创建人: 王伟权")
    @PostMapping(value = "/update")
    public InvokeResult updateContract(@Validated @RequestBody UpdateContractViewModel viewModel, BindingResult result) {

        if (result.hasErrors())
            return ValidateFailResult(result);
        Contract contract = contractService.getContract(viewModel.getId());
        ContractExtension contractExtension=contract.getContractExtension();
        BeanUtils.copyProperties(viewModel, contract);
        BeanUtils.copyProperties(viewModel, contractExtension);
        contract.setContractPeriod(DateUtils.getIntervalDays(contractExtension.getStartTime(),contractExtension.getEndTime()));

        contractService.updateContract(contract);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询合同列表", notes = "修改人: 张瑞兵")
    @PostMapping(value = "")
    public InvokeResult<PageList<ContractListViewMode>> queryContractList(@Validated @RequestBody ContractQueryRequest queryRequest, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);

        Page<Contract> pages = contractService.getContractList(queryRequest);
        PageList<ContractListViewMode> pageList = ApiBeanUtils.convertToPageList(pages, contract -> {
            ContractListViewMode contractViewModel = new ContractListViewMode();
            BeanUtils.copyProperties(contract, contractViewModel);
            contractViewModel.setOwnerName(getUserNickname(contract.getOwnerId()));
            contractViewModel.setSignerName(getUserNickname(contract.getSignerId()));
            contractViewModel.setCustomerName(getCustomerName(contract.getCustomerId()));
            ContractStatusEnum contractStatusEnum = contract.getContractStatusEnum();
            contractViewModel.setContractStatuName(contractStatusEnum.getDisplayName());
            return contractViewModel;
        });
        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value = "查询合同状态信息", notes = "创建人: 陈化静")
    @PostMapping(value = "/statusStatistics")
    public InvokeResult<ContractStatusViewModel> retrieveContractStatusStatistics() {
        ContractStatusViewModel viewModel = contractService.getContractStatusStatistics();
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "通过合同ID预览合同", notes = "创建人: 文丰")
    @PostMapping(value = "preview/{contractId}")
    public InvokeResult<String> previewContract(@PathVariable("contractId") String contractId) {
        if (StringUtils.isEmpty(contractId))
            return InvokeResult.Fail("合同ID不能为空");
        return InvokeResult.SuccessResult(previewService.getOrCreatePreview(contractId));
    }

    @ApiOperation(value = "往合同添加门店数据", notes = "创建人: 王伟权 data返回已经选中的门店数量")
    @PostMapping(value = "/store/add")
    public InvokeResult<Long> addContractStore(@Validated @RequestBody CreateContractStoreViewModel viewModel, BindingResult result) {

        if (!advertisementPriceConfigService.hasCompletedPriceConfig())
            return InvokeResult.Fail("暂无门店类别价格配置，请联系系统管理员");

        if (result.hasErrors())
            return ValidateFailResult(result);

        StoreTypeEnum storeTypeEnum = EnumUtils.toEnum(viewModel.getStoreType(), StoreTypeEnum.class);
        if (storeTypeEnum == null)
            return InvokeResult.Fail("请输入正确的门店类型");

        ContractStore store = new ContractStore();
        BeanUtils.copyProperties(viewModel, store);

        contractService.addContractStore(store);
        return InvokeResult.SuccessResult(contractService.getTotalStoresByType(store));
    }


    @ApiOperation(value = "删除合同门店数据", notes = "创建人: 王伟权")
    @PostMapping(value = "/store/delete")
    public InvokeResult deleteStore(@Validated @RequestBody ContractStoreViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);

        ContractStore contractStore = new ContractStore();
        BeanUtils.copyProperties(viewModel, contractStore);

        contractService.deleteContractStore(contractStore);
        return InvokeResult.SuccessResult();
    }


    @ApiOperation(value = "清空选择的门店", notes = "创建人:  王伟权")
    @PostMapping(value = "/store/empty")
    public InvokeResult emptyStores(@Validated @RequestBody EmptyContractStoreViewModel viewModel, BindingResult result) {

        if (result.hasErrors())
            return ValidateFailResult(result);

        StoreTypeEnum storeTypeEnum = EnumUtils.toEnum(viewModel.getStoreType(), StoreTypeEnum.class);
        if (storeTypeEnum == null)
            return InvokeResult.Fail("请输入正确的门店类型");

        contractService.deleteAllContractStores(viewModel.getContractId(), viewModel.getStoreType());
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "操作合同(code:-2 表示失败时备注说明不能为空！)", notes = "创建人: 李川")
    @PostMapping(value = "/operate")
    public InvokeResult operateContract(@Validated @RequestBody ContractOperationViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (!viewModel.isSuccessed() && StringUtils.isEmpty(viewModel.getRemark()))
            return InvokeResult.Fail("说明不能为空！", -2);
        //合同不能直接执行完成
        if(viewModel.getOperation().equals(ContractOperationEnum.Finish.getValue()) && viewModel.isSuccessed())
            return InvokeResult.Fail("不支持本操作！", -2);

        ContractOperationLog operationLog = new ContractOperationLog();
        BeanUtils.copyProperties(viewModel, operationLog);
        contractOperationService.operate(operationLog);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询合同详情", notes = "创建人: 陈化静")
    @GetMapping(value = "/{id}")
    public InvokeResult<ContractDetailViewModel> queryContractDetail(@PathVariable String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");

        ContractDetailViewModel contractDetailViewModel = new ContractDetailViewModel();
        Contract contract = contractService.getContract(id);
        BeanUtils.copyProperties(contract, contractDetailViewModel);
        ContractExtension contractExtension = contract.getContractExtension();
        BeanUtils.copyProperties(contractExtension, contractDetailViewModel);
        contractDetailViewModel.setHeadPortrait(contract.getCustomer() == null ? null : contract.getCustomer().getHeadPortrait());
        contractDetailViewModel.setCreator(getUserNickname(contract.getCreatorId()));
        contractDetailViewModel.setOwner(getUserNickname(contract.getOwnerId()));
        contractDetailViewModel.setSigner(getUserNickname(contract.getSignerId()));

        ContractStatusEnum statusEnum = contract.getContractStatusEnum();
        contractDetailViewModel.setContractStatusName(statusEnum.getDisplayName());
        contractDetailViewModel.setContractStatus(statusEnum.getValue());
        int totalStore = contractDetailViewModel.getStoreACount() + contractDetailViewModel.getStoreBCount() + contractDetailViewModel.getStoreCCount();
        contractDetailViewModel.setTotalStore(totalStore);
        double discountMoney = contractDetailViewModel.getMediumCost() + contractDetailViewModel.getProductCost() - contractDetailViewModel.getTotalCost();
        contractDetailViewModel.setDiscountMoney(discountMoney);

        ContractAdvertisementConfig contractAdvertisementConfig = contractAdvertisementConfigService.getContractAdvertisementConfig(contract.getId());
        BeanUtils.copyProperties(contractAdvertisementConfig, contractDetailViewModel);
        contractDetailViewModel.setDurationUnitName(EnumUtils.toEnum(contractAdvertisementConfig.getDurationUnit(), DurationUnitEnum.class).getDisplayName());
        contractDetailViewModel.setTimeUnitName(EnumUtils.toEnum(contractAdvertisementConfig.getTimeUnit(), DisplayTimeUnitEnum.class).getDisplayName());

        return InvokeResult.SuccessResult(contractDetailViewModel);
    }

    @ApiOperation(value = "查询客户的合同列表", notes = "创建人: 文丰")
    @PostMapping(value = "/{customerId}/list")
    public InvokeResult<PageList<ContractInfoViewModel>> getContractListByCustomerId(@PathVariable String customerId, @Validated @RequestBody BasePageInfo viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (StringUtils.isEmpty(customerId))
            return InvokeResult.Fail("客户ID不能为空");
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize());
        Page<Contract> pages = contractService.getContractListByCustomerId(customerId, pageable);
        List<String> accessedUserIds = userService.getAccessedUserIds();
        PageList<ContractInfoViewModel> resultList = ApiBeanUtils.convertToPageList(pages, contract ->
        {
            ContractInfoViewModel detailViewModel = new ContractInfoViewModel();
            ContractExtension contractExtension = contract.getContractExtension();
            BeanUtils.copyProperties(contract, detailViewModel);
            BeanUtils.copyProperties(contractExtension, detailViewModel);
            detailViewModel.setContractStatus(EnumUtils.toValueMap(ContractStatusEnum.class).get(contract.getContractStatus()));
            detailViewModel.setTotalCost(contract.getTotalCost());
            detailViewModel.setOwner(getUserNickname(contract.getOwnerId()));
            detailViewModel.setCanView(accessedUserIds.contains(contract.getOwnerId()));
            return detailViewModel;
        });
        return InvokeResult.SuccessResult(resultList);

    }


    @ApiOperation(value = "查询客户待执行合同列表", notes = "创建人: 李川")
    @PostMapping(value = "customer/pendingExecutionContracts")
    public InvokeResult<PageList<PendingExecutionContractListItem>> queryCustomerPendingExecutionContractList(@Validated @RequestBody ContractQueryRequest queryRequest, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        queryRequest.setContractStatus(ContractStatusEnum.PendingExecution.getValue());
        queryRequest.setJoinDescriptor(new JoinDescriptor().leftJoin(QContract.contract.contractExtension));
        Page<Contract> pages = contractService.getContractList(queryRequest);
        PageList<PendingExecutionContractListItem> pageList = ApiBeanUtils.convertToPageList(pages, contract -> {
            PendingExecutionContractListItem contractListItem = ApiBeanUtils.copyProperties(contract,PendingExecutionContractListItem.class);
            contractListItem.setOwnerName(getUserNickname(contract.getOwnerId()));
            contractListItem.setSignerName(getUserNickname(contract.getSignerId()));
            contractListItem.setCustomerName(getCustomerName(contract.getCustomerId()));
            contractListItem.setCustomerId(contract.getCustomerId());
            contractListItem.setStartTime(contract.getContractExtension().getStartTime());
            return contractListItem;
        });
        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value = "查看合同是否存在未执行或正在执行的广告", notes = "创建人: 王伟权")
    @GetMapping(value = "/hasUnDeliveryAdvertisement/{contractId}")
    public InvokeResult<Boolean> hasUnDeliveryAdvertisement(@PathVariable String contractId) {
        if(StringUtils.isEmpty(contractId))
            return InvokeResult.Fail("合同ID不能为空!");
        return InvokeResult.SuccessResult(contractService.hasUnDeliveryAdvertisement(contractId));
    }

}
