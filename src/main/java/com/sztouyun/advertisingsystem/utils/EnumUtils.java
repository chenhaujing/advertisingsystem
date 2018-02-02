package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import org.apache.calcite.linq4j.Linq4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class EnumUtils {
    public static <T extends EnumMessage<S>,S> T toEnum(S value, Class<T> enumClass)  {
        EnumMessage<S>[]  items = getEnumValues(enumClass);
        return (T) Linq4j.asEnumerable(items).firstOrDefault(item->item.getValue().equals(value));
    }

    public static <T extends EnumMessage<S>,S>  Map<S,String> toValueMap(Class<T> enumClass)  {
        EnumMessage<S>[]  items = getEnumValues(enumClass);
        Map<S,String> map=new HashMap<>();
        for (EnumMessage<S> item: items){
            map.put(item.getValue(),item.getDisplayName());
        }
        return map;
    }

    public static <T extends EnumMessage<S>,S>  boolean isValidValue(Class<T> enumClass,S value)  {
        EnumMessage<S>[]  items = getEnumValues(enumClass);
        return Linq4j.asEnumerable(items).any(i ->i.getValue().equals(value));
    }

    private static <T> EnumMessage[] getEnumValues(Class<T> enumClass){
        try {
            Method method = enumClass.getMethod("values");
            return  (EnumMessage[]) method.invoke(null, null);
        }catch (Exception ex){
            return new EnumMessage[0];
        }
    }
}
