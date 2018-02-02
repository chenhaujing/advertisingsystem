package com.sztouyun.advertisingsystem.common.annotation.validation;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.utils.EnumUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<EnumValue, Integer> {
    private Class<? extends EnumMessage<Integer>> enumClass;

    @Override
    public void initialize(EnumValue enumValue) {
        enumClass = enumValue.enumClass();
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return EnumUtils.isValidValue(enumClass,integer);
    }
}
