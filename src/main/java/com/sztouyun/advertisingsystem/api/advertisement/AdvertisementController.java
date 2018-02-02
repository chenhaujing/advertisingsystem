package com.sztouyun.advertisingsystem.api.advertisement;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.AdvertisementAreaCountInfo;
import com.sztouyun.advertisingsystem.model.advertisement.*;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractAdvertisementConfig;
import com.sztouyun.advertisingsystem.model.contract.ContractExtension;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementOperationService;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import com.sztouyun.advertisingsystem.service.contract.ContractAdvertisementConfigService;
import com.sztouyun.advertisingsystem.service.contract.ContractService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.*;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * Created by wenfeng on 2017/8/4.
 */

@Api("广告接口")
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController extends BaseApiController {
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private ContractAdvertisementConfigService contractAdvertisementConfigService;
    @Autowired
    private AdvertisementOperationService advertisementOperationService;
    @Autowired
    private ContractService contractService;

    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "所有广告状态", notes = "创建者：李川")
    @RequestMapping(value = "statuses", method = RequestMethod.GET)
    public InvokeResult<Map<Integer,String>> queryAdvertisementStatusList() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return InvokeResult.SuccessResult(EnumUtils.toValueMap(AdvertisementStatusEnum.class));
    }

    @ApiOperation(value = "新增广告", notes = "创建人：文丰")
    @PostMapping(value = "/create")
    public InvokeResult<String> createAdvertisement(@Validated @RequestBody CreateAdvertisementViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Advertisement advertisement=new Advertisement();
        BeanUtils.copyProperties(viewModel,advertisement);
        return InvokeResult.SuccessResult(advertisementService.createAdvertisement(advertisement));
    }

    @ApiOperation(value = "修改广告", notes = "创建人：文丰")
    @PostMapping(value = "/update")
    public InvokeResult updateAdvertisement(@Validated @RequestBody UpdateAdvertisementViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Advertisement advertisement=new Advertisement();
        BeanUtils.copyProperties(viewModel,advertisement);
        advertisementService.updateAdvertisement(advertisement);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询广告回显信息", notes = "创建人：文丰")
    @RequestMapping(value = "/{id}/editInfo", method = RequestMethod.GET)
    public InvokeResult<AdvertisementReapperViewModel> getAdvertisementEditInfo(@PathVariable("id") String id) {
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        Advertisement advertisement= advertisementService.getAdvertisement(id);
        AdvertisementReapperViewModel detailViewModel=new AdvertisementReapperViewModel();
        detailViewModel.setData(advertisement.getData());
        BeanUtils.copyProperties(advertisement,detailViewModel);
        return InvokeResult.SuccessResult(detailViewModel);
    }

    @ApiOperation(value = "删除广告信息", notes = "创建人：文丰")
    @PostMapping(value = "/{id}/delete")
    public InvokeResult delete(@PathVariable("id") String id){
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        advertisementService.deleteAdvertisement(id);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "统计广告状态信息", notes = "创建人: 文丰")
    @PostMapping(value = "/statusStatistics")
    public InvokeResult<AdvertisementStatusViewModel> retrieveContractStatusStatistics(@Validated @RequestBody AdvertisementStatusCountViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        AdvertisementStatusViewModel viewModelResult = advertisementService.getAdvertisementStatusStatistics(viewModel);
        return InvokeResult.SuccessResult(viewModelResult);
    }

    @ApiOperation(value = "查询广告列表", notes = "创建人：文丰")
    @PostMapping(value = "")
    public InvokeResult<PageList<AdvertisementListItemViewModel>> getAdvertisementList(@Validated @RequestBody AdvertisementPageInfoViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        viewModel.setJoinDescriptor(new JoinDescriptor().leftJoin(qAdvertisement.contract));
        Page<Advertisement> pages = advertisementService.queryAdvertisementList(viewModel);
        PageList<AdvertisementListItemViewModel> resultList= ApiBeanUtils.convertToPageList(pages, advertisement ->
        {
            AdvertisementListItemViewModel advertisementListItemView=new AdvertisementListItemViewModel();
            BeanUtils.copyProperties(advertisement,advertisementListItemView);
            setAdvertisementListWithContractInfo(advertisementListItemView,advertisement);
            advertisementListItemView.setAdvertisementStatusName(EnumUtils.toValueMap(AdvertisementStatusEnum.class).get(advertisement.getAdvertisementStatus()));
            return advertisementListItemView;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    private void setAdvertisementListWithContractInfo(AdvertisementListItemViewModel advertisementListItemView,Advertisement advertisement){
        Contract contract=advertisement.getContract();
        advertisementListItemView.setContractName(contract.getContractName());
        advertisementListItemView.setCustomerName(getCustomerName(contract.getCustomerId()));
        advertisementListItemView.setAdvertisementType(EnumUtils.toValueMap(MaterialTypeEnum.class).get(advertisement.getAdvertisementType()));
        advertisementListItemView.setTotalCost(NumberFormatUtil.format(contract.getTotalCost()));
        advertisementListItemView.setOwner(getUserNickname(contract.getOwnerId()));
        advertisementListItemView.setPeriod(DateUtils.getDiffMonth(advertisement.getStartTime(),advertisement.getEndTime()));
    }

    @ApiOperation(value = "查询广告状详情", notes = "创建人: 文丰")
    @GetMapping(value = "/{id}")
    public InvokeResult<DetailAdvertisementViewModel> getAdvertisement(@PathVariable("id") String id) {
        if(StringUtils.isEmpty(id))
            throw  new BusinessException("广告ID不能为空");
        Advertisement advertisement = advertisementService.getAdvertisement(id);
        return InvokeResult.SuccessResult( setDetailAdvertisementViewModel(advertisement));
    }

    private DetailAdvertisementViewModel setDetailAdvertisementViewModel(Advertisement advertisement){
        DetailAdvertisementViewModel detailAdvertisementViewModel=ApiBeanUtils.copyProperties(advertisement,DetailAdvertisementViewModel.class);
        detailAdvertisementViewModel.setCreator(getUserNickname(advertisement.getCreatorId()));
        detailAdvertisementViewModel.setCustomerName(getCustomerName(advertisement.getCustomerId()));
        detailAdvertisementViewModel.setPortrait(advertisement.getCustomer().getHeadPortrait());
        detailAdvertisementViewModel.setDeliveryOperator(getUserNickname(advertisement.getDeliveryOperatorId()));

        detailAdvertisementViewModel.setAdvertisementTypeName(EnumUtils.toValueMap(MaterialTypeEnum.class).get(advertisement.getAdvertisementType()));
        detailAdvertisementViewModel.setAdvertisementStatusName(EnumUtils.toValueMap(AdvertisementStatusEnum.class).get(advertisement.getAdvertisementStatus()));
        detailAdvertisementViewModel.setAdvertisementPeriod(DateUtils.formate(advertisement.getAdvertisementPeriod()));
        detailAdvertisementViewModel.setStartTime(advertisement.getStartTime());
        detailAdvertisementViewModel.setEndTime(advertisement.getEndTime());
        setContractInfo(detailAdvertisementViewModel,advertisement);
        //广告ID被合同ID覆盖
        detailAdvertisementViewModel.setId(advertisement.getId());
        //广告备注被覆盖
        detailAdvertisementViewModel.setRemark(advertisement.getRemark());
        setConfigInfo(detailAdvertisementViewModel);
        setDeliveryAndTakeOffInfo(detailAdvertisementViewModel,advertisement);
        return detailAdvertisementViewModel;
    }

    /**
     * 设置合同相关信息
     * @param detailAdvertisementViewModel
     * @param advertisement
     */
    public void setContractInfo(DetailAdvertisementViewModel detailAdvertisementViewModel,Advertisement advertisement){
        Contract contract=advertisement.getContract();
        ContractExtension contractExtension=contract.getContractExtension();
        BeanUtils.copyProperties(contract,detailAdvertisementViewModel);
        BeanUtils.copyProperties(contractExtension,detailAdvertisementViewModel);
        detailAdvertisementViewModel.setTotalStoreCount(contractExtension.getTotalStoreCount());
        detailAdvertisementViewModel.setTotalCost(NumberFormatUtil.format(contract.getTotalCost()));
        if(contractExtension.getDiscount()<1){
            detailAdvertisementViewModel.setIsDiscount(1);
        }
        detailAdvertisementViewModel.setDiscount(NumberFormatUtil.format(contractExtension.getDiscount()));
        //合同与广告起止时间的属性同名，会覆盖
        detailAdvertisementViewModel.setStartTime(advertisement.getStartTime());
        detailAdvertisementViewModel.setEndTime(advertisement.getEndTime());
        detailAdvertisementViewModel.setStartTimeOfContract(contractExtension.getStartTime());
        detailAdvertisementViewModel.setEndTimeOfContract(contractExtension.getEndTime());
        detailAdvertisementViewModel.setContractPeriod(DateUtils.formate(contract.getContractPeriod()));
        detailAdvertisementViewModel.setUsedContractPeriod(DateUtils.formate(contract.getUsedContractPeriod()));
    }

    /**
     * 设置配置信息
     * @param detailAdvertisementViewModel
     * @return
     */
    private DetailAdvertisementViewModel  setConfigInfo(DetailAdvertisementViewModel detailAdvertisementViewModel){
        String contractId=detailAdvertisementViewModel.getContractId();
        ContractAdvertisementConfig contractAdvertisementConfig=contractAdvertisementConfigService.getContractAdvertisementConfig(contractId);
        detailAdvertisementViewModel.setAdvertisementPosition(contractAdvertisementConfig.getSizeName());
        detailAdvertisementViewModel.setAdvertisementSize(contractAdvertisementConfig.getHorizontalResolution()+ Constant.STAR+contractAdvertisementConfig.getVerticalResolution());
        detailAdvertisementViewModel.setAdvertisementDuration(contractAdvertisementConfig.getDuration()+EnumUtils.toValueMap(DurationUnitEnum.class).get(contractAdvertisementConfig.getDurationUnit()));
        detailAdvertisementViewModel.setGetAdvertisementDisplayTimes(contractAdvertisementConfig.getDisplayTimesInfo());
        return detailAdvertisementViewModel;
    }

    /**
     * 设置投放及下架信息
     * @param detailAdvertisementViewModel
     * @param advertisement
     * @return
     */
    private DetailAdvertisementViewModel setDeliveryAndTakeOffInfo(DetailAdvertisementViewModel detailAdvertisementViewModel,Advertisement advertisement){
        detailAdvertisementViewModel.setDeliveryOperator(getUserNickname(advertisement.getDeliveryOperatorId()));
        if(advertisement.getAdvertisementStatus().equals(AdvertisementStatusEnum.TakeOff.getValue())){
            AdvertisementOperationLog advertisementOperationLog=advertisementOperationService.findTakeOffLog(advertisement.getId());
            detailAdvertisementViewModel.setTakeOffTime(advertisementOperationLog.getCreatedTime());
            detailAdvertisementViewModel.setTakeOffOperator(getUserNickname(advertisementOperationLog.getCreatorId()));
            detailAdvertisementViewModel.setTakeOffRemark(advertisementOperationLog.getRemark());
        }
        return detailAdvertisementViewModel;
    }

    @ApiOperation(value = "操作广告(code:-2 表示失败时备注说明不能为空！)", notes = "创建人: 李川")
    @PostMapping(value = "/operate")
    public InvokeResult operateAdvertisement(@Validated @RequestBody AdvertisementOperationViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (!viewModel.isSuccessed() && StringUtils.isEmpty(viewModel.getRemark()))
            return InvokeResult.Fail("说明不能为空！", -2);
        //广告不能直接执行完成
        if(viewModel.getOperation().equals(AdvertisementOperationEnum.Finish.getValue()) && viewModel.isSuccessed())
            return InvokeResult.Fail("不支持本操作！", -2);

        AdvertisementOperationLog operationLog = new AdvertisementOperationLog();
        BeanUtils.copyProperties(viewModel, operationLog);
        advertisementOperationService.operate(operationLog);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "统计上下架, 高风险广告数据量", notes = "创建人：王伟权")
    @PostMapping(value = "/deliveringStatistics")
    public InvokeResult<List<AdvertisementStatusStatisticsViewModel>> getAdvertisementDeliveringStatistics(@Validated @RequestBody AdvertisementStatusCountViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(advertisementService.getAdvertisementDeliveringStatistics(viewModel));
    }
	
	@ApiOperation(value = "广告投放地区统计列表", notes = "创建人: 文丰")
    @PostMapping(value = "/areaCountList")
    public InvokeResult<PageList<AdvertisementReportInfoViewModel>> getAdvertisementAreaList(@Validated @RequestBody AdvertisementAreaPageInfoViewModel viewModel,BindingResult result ) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable=new PageRequest(viewModel.getPageIndex(),viewModel.getPageSize(),new QSort(QStoreInfo.storeInfo.cityId.asc()));
        Page<AdvertisementAreaCountInfo> page=advertisementService.getAdvertisementReportInfo(viewModel.getId(),pageable);
        PageList<AdvertisementReportInfoViewModel> resultList = ApiBeanUtils.convertToPageList(page, advertisementAreaCountInfo -> {
            AdvertisementReportInfoViewModel item =ApiBeanUtils.copyProperties(advertisementAreaCountInfo,AdvertisementReportInfoViewModel.class);
            item.setCityName(areaService.getAreaNameFromCache(advertisementAreaCountInfo.getCityId()));
            item.setProvinceName((areaService.getParentAreaNameFromCache(advertisementAreaCountInfo.getCityId())));
            return item;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "合同广告投放统计", notes = "创建人: 王伟权")
    @PostMapping("/advertisingHistory")
    public InvokeResult<ContractAdvertisementDeliveryViewModel> getContractAdvertisementDeliveryStatistics(@Validated @RequestBody AdvertisementHistoryRequestViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);

        Contract contract = contractService.getContract(viewModel.getContractId());
        if (contract == null)
            throw new BusinessException("合同数据不存在");

        ContractAdvertisementDeliveryViewModel contractAdvertisementDeliveryViewModel = null;
        int advertisementPeriod = DateUtils.getIntervalDays(viewModel.getStartTime(), viewModel.getEndTime());

        ContractAdvertisementDeliveryStatistics contractAdvertisementDeliveryStatistics = advertisementService.getContractAdvertisementDeliveryStatistics(contract, advertisementPeriod);
        if(contractAdvertisementDeliveryStatistics != null) {
            contractAdvertisementDeliveryViewModel = ApiBeanUtils.copyProperties(contractAdvertisementDeliveryStatistics, ContractAdvertisementDeliveryViewModel.class);
            contractAdvertisementDeliveryViewModel.setContractName(contract.getContractName());
            contractAdvertisementDeliveryViewModel.setStartTime(contract.getContractExtension().getStartTime());
            contractAdvertisementDeliveryViewModel.setEndTime(contract.getContractExtension().getEndTime());
            contractAdvertisementDeliveryViewModel.setAdvertisePeriod(contract.getContractExtension().getTotalMonths());
            contractAdvertisementDeliveryViewModel.setHasAdvertisedPeriod(contract.getUsedContractPeriod());
        }

        return InvokeResult.SuccessResult(contractAdvertisementDeliveryViewModel);
    }


}