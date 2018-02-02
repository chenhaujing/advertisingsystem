package com.sztouyun.advertisingsystem.api.system;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by szty on 2017/7/25.
 */
@Api(value = "地区管理接口")
@RestController
@RequestMapping("/api/area")
public class AreaController extends BaseApiController {

    @Autowired
    private AreaService areaService;

    @ApiOperation(value = "查询下级地区", notes = "创建人：张瑞兵")
    @GetMapping("/{areaId}/sub")
    public InvokeResult<List<AreaViewModel>> getSubAreas(@PathVariable("areaId") String areaId) {
        List<Area> areas = areaService.getAllAreasFromCache();
        List<AreaViewModel> list = Linq4j.asEnumerable(areas).where(a -> a.getParentId().equals(areaId))
                .select(a -> ApiBeanUtils.copyProperties(a, AreaViewModel.class)).toList();
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "查询有客户的地区", notes = "创建人：张瑞兵")
    @PostMapping("/existsCustomer")
    public InvokeResult<List<AreaViewModel>> getAreasIfExistsCustomer() {
        List<Area> areas = areaService.getExistsCustomerAreas();
        List<AreaViewModel> list = ApiBeanUtils.convertToTreeList(areas, area -> ApiBeanUtils.copyProperties(area, AreaViewModel.class), Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "查询包含门店信息的地区", notes = "创建人：王英峰")
    @PostMapping("/containStoreInfo")
    public InvokeResult<List<AreaViewModel>> getAreaByContainStoreInfo() {
        List<Area> areas = areaService.getAreaByContainStoreInfo();
        List<AreaViewModel> list = ApiBeanUtils.convertToTreeList(areas, area -> ApiBeanUtils.copyProperties(area, AreaViewModel.class), Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

}
