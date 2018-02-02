package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum ContractOperationEnum  implements EnumMessage<Integer> {
    Submit(1,"提交审批"),
    Auditing(2,"审核"),
    Sign(3,"签约"),
    Finish(4,"完成"),
    BeginExecute(11,"开始执行"),
    StageFinish(12,"阶段完成");

    private Integer value;
    private String displayName;

    ContractOperationEnum(Integer value,String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
