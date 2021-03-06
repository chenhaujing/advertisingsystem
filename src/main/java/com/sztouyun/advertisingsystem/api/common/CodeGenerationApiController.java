package com.sztouyun.advertisingsystem.api.common;

import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

@Api(value = "编码生成接口")
@RestController
@RequestMapping("/api/code")
public class CodeGenerationApiController {

    @Autowired
    private CodeGenerationService codeGenerationService;

    @ApiOperation(value = "创建编码", notes = "1：合同编号 ，创建人: 王伟权")
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public InvokeResult<String> generateCode(@RequestParam int codeType) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return InvokeResult.SuccessResult(codeGenerationService.generateCode(EnumUtils.toEnum(codeType,CodeTypeEnum.class)));
    }
}

