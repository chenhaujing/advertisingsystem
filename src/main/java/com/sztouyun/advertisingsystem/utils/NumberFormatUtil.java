package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.common.Constant;

import java.math.BigDecimal;

/**
 * Created by wenfeng on 2017/9/14.
 */
public class NumberFormatUtil {
    public static Double format(Double src,Integer scale){
        return new BigDecimal(src).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static Double format(Double src){
        return new BigDecimal(src).setScale(Constant.SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal format(BigDecimal src,Integer scale){
        return src.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }
    public static BigDecimal format(BigDecimal src){
        return src.setScale(Constant.SCALE, BigDecimal.ROUND_HALF_UP);
    }
}
