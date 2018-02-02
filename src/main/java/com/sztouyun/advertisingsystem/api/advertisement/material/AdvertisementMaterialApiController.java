package com.sztouyun.advertisingsystem.api.advertisement.material;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.advertisement.material.AdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.service.advertisement.material.AdvertisementMaterialService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.DetailAdvertisementDurationConfigViewModel;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.AdvertisementMaterialPageInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.AdvertisementMaterialStatisticsViewModel;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.DetailAdvertisementMaterialViewModel;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api("广告素材接口")
@RestController
@RequestMapping("/api/advertisement/material")
public class AdvertisementMaterialApiController extends BaseApiController {

    @Autowired
    private AdvertisementMaterialService advertisementMaterialService;

    @ApiOperation(value = "上传素材, 包括文本内容", notes = "创建人: 王伟权")
    @PostMapping(value = "/upload")
    public InvokeResult<DetailAdvertisementMaterialViewModel> uploadMaterial(@RequestParam String contractId, @RequestParam(required = true) String customerId, @RequestParam(required = true) Integer materialType, MultipartFile file) {

        if (StringUtils.isEmpty(customerId))
            return InvokeResult.Fail("客户信息不能为空");
        MaterialTypeEnum materialTypeEnum = EnumUtils.toEnum(materialType, MaterialTypeEnum.class);
        if (materialTypeEnum == null || materialTypeEnum.equals(MaterialTypeEnum.Text))
            return InvokeResult.Fail("请输入有效的媒体类型");

        AdvertisementMaterial advertisementMaterial = advertisementMaterialService.uploadMaterial(contractId, customerId, materialType, file);
        return InvokeResult.SuccessResult(ApiBeanUtils.copyProperties(advertisementMaterial, DetailAdvertisementMaterialViewModel.class));
    }

    @ApiOperation(value = "查看素材", notes = "创建人: 王伟权")
    @PostMapping(value = "")
    public InvokeResult<PageList<DetailAdvertisementMaterialViewModel>> getAllMaterials(@Validated @RequestBody AdvertisementMaterialPageInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);

        MaterialTypeEnum materialTypeEnum = EnumUtils.toEnum(viewModel.getMaterialType(), MaterialTypeEnum.class);
        if (materialTypeEnum == null)
            return InvokeResult.Fail("请输入有效的媒体类型");

        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize());
        Page<AdvertisementMaterial> pages = advertisementMaterialService.getAllMaterials(pageable, viewModel.getCustomerId(), viewModel.getMaterialType());
        PageList<DetailAdvertisementMaterialViewModel> resultList = ApiBeanUtils.convertToPageList(pages, advertisementMaterial -> {
            return ApiBeanUtils.copyProperties(advertisementMaterial, DetailAdvertisementMaterialViewModel.class);
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "查看素材统计信息", notes = "创建人: 王伟权")
    @GetMapping(value = "/{customerId}")
    public InvokeResult<List<AdvertisementMaterialStatisticsViewModel>> getMaterialStatistics(@PathVariable String customerId) {
        if(StringUtils.isEmpty(customerId))
            return InvokeResult.Fail("客户信息不存在");

        return InvokeResult.SuccessResult(advertisementMaterialService.getMaterialStatistics(customerId));
    }
}
