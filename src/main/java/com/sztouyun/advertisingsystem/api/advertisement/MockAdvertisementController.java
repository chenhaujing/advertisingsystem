package com.sztouyun.advertisingsystem.api.advertisement;


import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.mock.ModifyAdvertisementStartDeliveryTimeRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Profile({"local","dev","test","stage"})
@Api("模拟广告接口")
@RestController
@RequestMapping("/api/mock/advertisement")
public class MockAdvertisementController  extends BaseApiController {
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "修改执行中广告开始执行时间", notes = "创建者：李川")
    @RequestMapping(value = "/modifyStartDeliveryTime", method = RequestMethod.POST)
    public InvokeResult modifyAdvertisementStartTime(@Validated @RequestBody ModifyAdvertisementStartDeliveryTimeRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);

        Advertisement advertisement =advertisementRepository.findOne(request.getAdvertisementId());
        if(advertisement == null)
            return InvokeResult.Fail("广告不存在");
        if(!advertisement.getAdvertisementStatusEnum().equals(AdvertisementStatusEnum.Delivering))
            return InvokeResult.Fail("广告状态必须为投放中");
        Date effectiveStartTime =DateUtils.getDateAccurateToMinute(request.getStartDeliveryTime());
        advertisement.setEffectiveStartTime(effectiveStartTime);
        Contract contract =advertisement.getContract();
        Date expectedDueDay = DateUtils.addDays(effectiveStartTime,contract.getContractPeriod()-contract.getUsedContractPeriod());
        advertisement.setExpectedDueDay(expectedDueDay);
        advertisementRepository.save(advertisement);
        return InvokeResult.SuccessResult();
    }
}
