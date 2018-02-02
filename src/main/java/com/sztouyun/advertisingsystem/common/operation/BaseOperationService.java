package com.sztouyun.advertisingsystem.common.operation;

import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseOperationService<TData> extends BaseService implements IOperation<TData>{
    protected abstract void onOperating(TData data,IOperationCollection<TData> operationCollection);

    protected abstract void onOperated(TData data,IOperationCollection<TData> operationCollection);

    @Override
    public void operate(TData data) {
        operate(data,null);
    }

    @Transactional
    public void operate(TData data,Class<? extends IOperation>... operationClasses) {
        IOperationCollection<TData> operationCollection = new OperationCollection<>();
        onOperating(data,operationCollection);
        onOperated(data,operationCollection);
        if(operationClasses != null){
            for (Class<? extends IOperation> operation:operationClasses){
                operationCollection.remove(operation);
            }
        }
        operationCollection.operate(data);
    }
}
