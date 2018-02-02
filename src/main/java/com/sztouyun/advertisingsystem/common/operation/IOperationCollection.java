package com.sztouyun.advertisingsystem.common.operation;

public interface IOperationCollection<T> extends IOperation<T>{
    <TOperation extends IOperation<T>> OperationCollection add(Class<TOperation> operationClass);
    <TData,TOperation extends IOperation<TData>> OperationCollection add(Class<TOperation> operationClass,TData data);
    <TOperation extends IOperation> OperationCollection remove(Class<TOperation> operationClass);
}
