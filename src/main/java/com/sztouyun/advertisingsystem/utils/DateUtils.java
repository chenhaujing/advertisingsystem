package com.sztouyun.advertisingsystem.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wenfeng on 2017/8/2.
 */
public class DateUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        return calendar;
    }

    public static String getCurrentFormat() {
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String getCurrentFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 获取前一天日期
     *
     * @param format
     * @return
     */
    public static String getYesterdayFormat(String format) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        cal.add(Calendar.DATE, -1);
        Date time = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }


    /**
     * 获取时间段间隔天数
     *
     * @param sDate
     * @param eDate
     * @return
     */
    public static int getIntervalDays(Date sDate, Date eDate) {
        try {
            long timeSubtraction = eDate.getTime() - sDate.getTime();
            long dayMilliSecond = 24 * 60 * 60 * 1000;
            long day = timeSubtraction/ dayMilliSecond;
            long mod = timeSubtraction % dayMilliSecond;
            if( mod != 0) ++day;
            return (int) day;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @param format
     * @return date
     */
    public static Date strToDate(String str, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期是否在同年同月
     *
     * @param sDate
     * @param eDate
     * @return
     */
    public static boolean equalsDate(Date sDate, Date eDate) {

        Calendar sCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        sCalendar.setTime(sDate);
        Calendar eCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        eCalendar.setTime(eDate);

        return sCalendar.get(Calendar.YEAR) == eCalendar.get(Calendar.YEAR) && sCalendar.get(Calendar.MONTH) == eCalendar.get(Calendar.MONTH);
    }

    /**
     * 获取月初
     *
     * @param calendar
     * @return
     */
    public static String getStartMonth(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return sdf.format(calendar.getTime());
    }

    /**
     * 获取月末
     *
     * @param calendar
     * @return
     */
    public static String getEndMonth(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);

        return sdf.format(calendar.getTime());
    }

    /**
     * 获取时间段内的每月月初、月末
     *
     * @param startDateStr (格式:YYYY-MM-dd)
     * @param endDateStr   (格式:YYYY-MM-dd)
     * @return String[] (0:月初, 1:月末, 格式:YYYY-MM-dd)
     */
    public static List<String[]> getMonthArrayList(String startDateStr, String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String[]> reList = new ArrayList<>();
        String[] strArray;
        Date startDate = strToDate(startDateStr, "yyyy-MM-dd");
        Date endDate = strToDate(endDateStr, "yyyy-MM-dd");
        if (equalsDate(startDate, endDate)) { //同年同月
            strArray = new String[2];
            strArray[0] = startDateStr;
            strArray[1] = endDateStr;
            reList.add(strArray);
        } else {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            calendar.setTime(startDate);
            strArray = new String[2];
            strArray[0] = startDateStr;
            strArray[1] = getEndMonth(calendar);
            reList.add(strArray);
            calendar.add(Calendar.MONTH, 1);

            while (calendar.getTime().before(endDate) && !equalsDate(calendar.getTime(), endDate)) {//判断是否到结束日期
                String str = sdf.format(calendar.getTime());
                strArray = new String[2];
                strArray[0] = getStartMonth(calendar);
                strArray[1] = getEndMonth(calendar);
                reList.add(strArray);

                calendar.add(Calendar.MONTH, 1);
            }

            if (equalsDate(calendar.getTime(), endDate)) { //日期比較后的同年同月
                strArray = new String[2];
                strArray[0] = getStartMonth(calendar);
                strArray[1] = endDateStr;
                reList.add(strArray);
            }
        }

        return reList;
    }

    public static Date getMaxDate() {
        return strToDate("9999-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getMinDate() {
        return strToDate("1970-01-01 00:00:000", "yyyy-MM-dd HH:mm:ss");
    }

    public static  String getDiffMonth(Date start,Date end){
        int days=getIntervalDays(start,end);
        return  new BigDecimal(days/30).setScale(0, BigDecimal.ROUND_HALF_UP)+"个月";
    }

    public static Date getDateAccurateToMinute(Date date){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static  Date addDays(Date date, int days){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,days);
        return calendar.getTime();
    }

    /**
     * 格式化XX月XX天
     * @param days
     * @return
     */
    public static String formate(Integer days){
        StringBuffer result=new StringBuffer();
        int month=days/30;
        int leftDay=days%30;
        if(month>0){
            result.append(month).append("个月");
        }
        if(leftDay>0){
            if(leftDay<10 && month>0){
                result.append("0");
            }
            result.append(leftDay).append("天");
        }
        return result.toString();
    }
}