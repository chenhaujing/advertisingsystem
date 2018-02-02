package com.sztouyun.advertisingsystem.common.operation;


import com.sztouyun.advertisingsystem.utils.SpringUtil;
import org.apache.calcite.linq4j.Linq4j;

import java.util.ArrayList;
import java.util.List;

public class OperationCollection<T> implements IOperationCollection<T> {
    private List<OperationInfo> operationInfos = new ArrayList<>();

    @Override
    public void operate(T data) {
        for (OperationInfo operationInfo : operationInfos){
            operationInfo.getOperation().operate(operationInfo.getData()==null ? data : operationInfo.getData());
        }
    }

    public <TOperation extends IOperation<T>> OperationCollection add(Class<TOperation> operationClass){
        IOperation operation = SpringUtil.getBean(operationClass);
        if(operation !=null){
            operationInfos.add(new OperationInfo<T>(operation,null));
        }
        return this;
    }

    @Override
    public <TData, TOperation extends IOperation<TData>> OperationCollection add(Class<TOperation> operationClass, TData data) {
        IOperation<TData> operation = SpringUtil.getBean(operationClass);
        if(operation !=null){
            operationInfos.add(new OperationInfo<>(operation ,data));
        }
        return this;
    }

    public <TOperation extends IOperation> OperationCollection remove(Class<TOperation> operationClass){
        IOperation operation = SpringUtil.getBean(operationClass);
        if(operation !=null){
            operationInfos = Linq4j.asEnumerable(operationInfos).where(info->!info.getOperation().equals(operation)).toList();
        }
        return this;
    }
}

class OperationInfo<TData>{
    private IOperation<TData> operation;
    private TData data;

    public OperationInfo(IOperation<TData> operation, TData data) {
        this.operation = operation;
        this.data = data;
    }

    public IOperation<TData> getOperation() {
        return operation;
    }

    public void setOperation(IOperation<TData> operation) {
        this.operation = operation;
    }

    public TData getData() {
        return data;
    }

    public void setData(TData data) {
        this.data = data;
    }
}
