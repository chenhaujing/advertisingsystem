package com.sztouyun.advertisingsystem.service.common.impl;


import com.sztouyun.advertisingsystem.mapper.CodeGenerationMapper;
import com.sztouyun.advertisingsystem.model.common.CodeRule;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.repository.common.CodeGenerationRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeGenerationServiceImpl extends BaseService implements CodeGenerationService {
    private static final int INIT_CODE_NUMBER = 1;//初始化编码

    @Autowired
    private CodeGenerationMapper codeGenerationMapper;

    @Autowired
    private CodeGenerationRepository codeGenerationRepository;

    @Override
    public String generateCode(CodeTypeEnum codeTypeEnum) {
        return generateCode(codeTypeEnum.toString(),6);
    }

    @Override
    public String generateCode(String codeType,int length) {
        CodeRule codeRule = codeGenerationMapper.findByCodeType(codeType);

        if (codeRule == null) {
            codeRule = new CodeRule();
            codeRule.setCodeNumber(INIT_CODE_NUMBER);
            codeRule.setCodeType(codeType);
        } else {
            int codeNumber = codeRule.getCodeNumber();
            codeRule.setCodeNumber(++codeNumber);
        }

        codeGenerationRepository.save(codeRule);
        return codeType + String.format("%0"+length+"d", codeRule.getCodeNumber());
    }
}
