package com.sztouyun.advertisingsystem.common.operation;

public interface INoDataOperation extends IOperation{
    @Override
   default void operate(Object data){
        operate();
    }

    void operate();
}
