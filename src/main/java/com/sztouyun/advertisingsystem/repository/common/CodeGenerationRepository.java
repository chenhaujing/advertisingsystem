package com.sztouyun.advertisingsystem.repository.common;

import com.sztouyun.advertisingsystem.model.common.CodeRule;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface CodeGenerationRepository extends BaseRepository<CodeRule> {

    @Query(value = "select * from code_rule where code_type=?1 FOR UPDATE  ",nativeQuery = true)
    CodeRule findByCodeType(String codeTypeEnum);
}
