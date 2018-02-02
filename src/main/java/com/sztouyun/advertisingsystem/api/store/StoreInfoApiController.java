package com.sztouyun.advertisingsystem.api.store;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementPriceConfigService;
import com.sztouyun.advertisingsystem.service.store.IStoreService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.store.AutoChooseStoreInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoPageInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "门店信息接口")
@RestController
@RequestMapping("/api/store")
public class StoreInfoApiController extends BaseApiController {
    @Autowired
    private IStoreService iStoreService;
    @Autowired
    private AdvertisementPriceConfigService advertisementPriceConfigService;

    @ApiOperation(value="合同选择门店",notes = "创建人：王英峰")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public InvokeResult<PageList<StoreInfoViewModel>> getStoreInfoList(@Validated @RequestBody StoreInfoPageInfoViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        StoreTypeEnum storeTypeEnum = EnumUtils.toEnum(viewModel.getStoreType(), StoreTypeEnum.class);
        if(storeTypeEnum == null)
            return InvokeResult.Fail("请输入正确的门店类型");

        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(),viewModel.getPageSize());
        Page<StoreInfo> page = iStoreService.findStoreListByArea(viewModel.getContractId(), viewModel.getStoreType(), viewModel.getAreaIds(), pageable);
        final int[] chooseCount = {0};
        PageList<StoreInfoViewModel> resultList = ApiBeanUtils.convertToPageList(page, storeInfo->
        {
            StoreInfoViewModel storeInfoViewModel = new StoreInfoViewModel();
            if(storeInfo.getIsChoose() > 0){
                chooseCount[0]++;
            }
            BeanUtils.copyProperties(storeInfo,storeInfoViewModel);
            return storeInfoViewModel;
        });
        resultList.setChooseCount(chooseCount[0]);
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value="合同选择门店一键选取",notes = "修改人：王伟权  data返回当前操作选中的数量, 若返回0, 表示没有变化")
    @RequestMapping(value = "/autoChoose", method = RequestMethod.POST)
    public InvokeResult autoChoose(@Validated @RequestBody AutoChooseStoreInfoViewModel viewModel, BindingResult result) {
        if(!advertisementPriceConfigService.hasCompletedPriceConfig())
            return InvokeResult.Fail("暂无门店类别价格配置，请联系系统管理员");

        if(result.hasErrors())
            return ValidateFailResult(result);
        if(viewModel.getRecordCount() <= 0)
            return InvokeResult.Fail("一键选取记录数必须大于0");
        StoreTypeEnum storeTypeEnum = EnumUtils.toEnum(viewModel.getStoreType(), StoreTypeEnum.class);
        if(storeTypeEnum == null)
            return InvokeResult.Fail("请输入正确的门店类型");
        int usedCount = iStoreService.chooseStoreIdTop(viewModel.getContractId(), viewModel.getStoreType(), viewModel.getAreaIds(), viewModel.getRecordCount());

        return InvokeResult.SuccessResult(iStoreService.getChooseStoresCount(viewModel.getContractId(), viewModel.getStoreType()));
    }
}
