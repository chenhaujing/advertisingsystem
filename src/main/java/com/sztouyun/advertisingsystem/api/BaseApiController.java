package com.sztouyun.advertisingsystem.api;

import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.controller.BaseController;
import com.sztouyun.advertisingsystem.service.account.UserService;
import com.sztouyun.advertisingsystem.service.customer.CustomerService;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BaseApiController extends BaseController {
    @Autowired
    protected UserService userService;

    @Autowired
    protected AreaService areaService;

    @Autowired
    protected CustomerService customerService;

    protected InvokeResult ValidateFailResult(BindingResult result) {
        FieldError fieldError = result.getFieldErrors().get(0);
        return InvokeResult.Fail(fieldError.isBindingFailure() ? fieldError.getField() + "参数类型错误！" : fieldError.getDefaultMessage(), 100);
    }

    public String getUserNickname(String userId) {
        if (StringUtils.isEmpty(userId))
            return "";
        return userService.getNicknameFromCache(userId);
    }

    public String getAreaName(String areaId) {
        if (StringUtils.isEmpty(areaId))
            return "";
        return areaService.getAreaNameFromCache(areaId);
    }

    public String getCustomerName(String customerId) {
        if (StringUtils.isEmpty(customerId))
            return "";
        return customerService.getCustomerNameFromCache(customerId);
    }
}
