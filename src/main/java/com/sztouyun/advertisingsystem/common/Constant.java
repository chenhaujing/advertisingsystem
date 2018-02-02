package com.sztouyun.advertisingsystem.common;

public class Constant {

    /**
     * 门店信息选择
     */
    public static final String STOREINFO_SELECT = "StoreInfoMapping";

    /**
     * 树形结构顶级ID
     */
    public static final String TREE_ROOT_ID = "0";

    public static final int INTEGER_MAX = 999999999;


    /**
     * 邮箱正则表达式
     */
    public static final String REGEX_EMAIL = "^([\\w-_]+(?:\\.[\\w-_]+)*)@((?:[a-z0-9]+(?:-[a-zA-Z0-9]+)*)+\\.[a-z]{2,6})$";

    /**
     * 电话号码 包括固话 正则表达式
     */
    public static final String REGEX_PHONE = "(^(^[1][3,4,5,7,8][0-9]{9}$|(\\d{4}|\\d{3})-(\\d{7,8}))$)";

    public static final String REG_PHONE="^[1]([3,4,5,7,8]\\d|[6,9]{2})\\d{8}$";

    /**
     * 银行卡号码 正则表达式
     */
    public static final String REGEX_BANK_NUMBER = "^\\d+$";

    public static final String DATA_YMD = "YYYY-MM-dd";

    public static final String DATETIME="yyyy-MM-dd HH:mm:ss";

    public static final String DATA_YMD_CN = "YYYY年MM月dd日";

    public static final String TIME_YMDH_CN = "YYYY年MM月dd日HH点";

    public static final String TIME_YMDHM_CN = "YYYY年MM月dd日 HH:mm";

    public static final String WAR="war";

    public static final String JAR="jar";

    public static final Integer DOWNLOAD_TIMEOUT=6000;

    /**
     * 设置保留两位小数
     */
    public static Integer SCALE = 2;

    public static final String PER="/";

    public static final String STAR="*";

    public static final String SUCCESS_REMARK="定时任务执行完成";

}
