package com.jzz.newspeciality.java8.time.temporal.temporalfield;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;

/**
 * 日期时间字段枚举详解
 * enum ChronoField implements TemporalField
 * @author jzz
 * @date 2019/6/6
 */
public class ChronoFieldDetail {
    /**
     * 名字
     */
    private static  String name;
    /**
     * 基本单位 {@code ChronoUnit}
     */
    private static  TemporalUnit baseUnit;
    /**
     * 范围单位 {@code ChronoUnit}
     */
    private static  TemporalUnit rangeUnit;
    /**
     * 范围值
     */
    private static  ValueRange range;
    /**
     * 展示的方式
     */
    private static  String displayNameKey;
    public static void main(String[] args) {
        /**
         * 一秒钟多少纳秒 范围 0, 999_999_999
         */
        ChronoField nanoOfSecond = ChronoField.NANO_OF_SECOND;
        /**
         * 一天多少纳秒 范围 0, 86399999999999
         */
        ChronoField nanoOfDay = ChronoField.NANO_OF_DAY;
        /**
         * 一秒钟多少微秒 范围 0, 999_999
         */
        ChronoField microOfSecond = ChronoField.MICRO_OF_SECOND;
        /**
         * 一天多少微秒 范围 0, 86399999999999
         */
        ChronoField microOfDay = ChronoField.MICRO_OF_DAY;
        /**
         * 一秒钟多少毫秒 范围 0, 999
         */
        ChronoField milliOfSecond = ChronoField.MILLI_OF_SECOND;
        /**
         * 一天多少毫秒 范围 0, 86399999
         */
        ChronoField milliOfDay = ChronoField.MILLI_OF_DAY;
        /**
         * 一分钟多少秒 范围 0, 59
         */
        ChronoField secondOfMinute = ChronoField.SECOND_OF_MINUTE;
        /**
         * 一天多少秒 范围 0, 86399
         */
        ChronoField secondOfDay = ChronoField.SECOND_OF_DAY;
        /**
         * 一小时多少分钟 范围 0, 59
         */
        ChronoField minuteOfHour = ChronoField.MINUTE_OF_HOUR;
        /**
         * 一天多少分钟 范围 0, 1439
         */
        ChronoField minuteOfDay = ChronoField.MINUTE_OF_DAY;
        /**
         * AM/PM多少小时 范围 0, 11
         */
        ChronoField hourOfAmpm = ChronoField.HOUR_OF_AMPM;
        /**
         * 一天多少小时 范围 0, 23
         */
        ChronoField hourOfDay = ChronoField.HOUR_OF_DAY;
        /**
         * 上午/下午时钟小时数 范围 1, 11
         */
        ChronoField clockHourOfAmpm = ChronoField.CLOCK_HOUR_OF_AMPM;
        /**
         * 一天时钟小时数 范围 1, 24
         */
        ChronoField clockHourOfDay = ChronoField.CLOCK_HOUR_OF_DAY;
        /**
         * 一天多少个AP/PM 范围 0,1
         */
        ChronoField ampmOfDay = ChronoField.AMPM_OF_DAY;
        /**
         * 一个月内对齐的星期几。范围1,7
         * 1到7号映射为1-7,8-14映射为1-7类推
         */
        ChronoField alignedDayOfWeekInMonth = ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
        /**
         * 一年内对齐的星期几。范围1,7
         * 1到7号映射为1-7,8-14映射为1-7类推
         */
        ChronoField alignedDayOfWeekInYear = ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
        /**
         * 一个月内对齐的第几周。范围1,5
         * 1到7号映射为1-7,8-14映射为1-7类推
         */
        ChronoField alignedWeekOfMonth = ChronoField.ALIGNED_WEEK_OF_MONTH;
        /**
         * 一年内对齐的第几周。范围1,53
         * 1到7号映射为1-7,8-14映射为1-7类推
         */
        ChronoField alignedWeekOfYear = ChronoField.ALIGNED_WEEK_OF_YEAR;
        /**
         * 一周内第几天 范围1,7
         */
        ChronoField dayOfWeek = ChronoField.DAY_OF_WEEK;
        /**
         * 一个月内第几天 范围1,31
         */
        ChronoField dayOfMonth = ChronoField.DAY_OF_MONTH;
        /**
         * 一年内第几天 范围1,366
         */
        ChronoField dayOfYear = ChronoField.DAY_OF_YEAR;
        /**
         * 到1970-01-01（ISO）为零的连续天数。 范围 -365243219162L, 365241780471L
         */
        ChronoField epochDay = ChronoField.EPOCH_DAY;
        /**
         * LocalDate.now() 2019-06-10
         * 18057
         */
        epochDay.getFrom(LocalDate.now());
        /**
         * 一年内第几个月 范围1,12
         */
        ChronoField monthOfYear = ChronoField.MONTH_OF_YEAR;
        /**
         * 从0年开始计算当前日期第多少月
         */
        ChronoField prolepticMonth = ChronoField.PROLEPTIC_MONTH;
        /**
         * LocalDate.now() 2019-06-10
         * 24233
         */
        prolepticMonth.getFrom(LocalDate.now());
        /**
         * 这个时代的一年
         * 日期的完整模型需要四个概念
         * 时代 {@code ERA}
         * 年 {@code YEAR_OF_ERA}
         * 月 {@code MONTH_OF_YEAR}
         * 日 {@code DAY_OF_MONTH}
         *
         */
        ChronoField yearOfEra = ChronoField.YEAR_OF_ERA;
        /**
         * 表示某一年
         */
        ChronoField year = ChronoField.YEAR;
        /**
         * 表示时代
         * 在默认的ISO日历系统中，定义了两个时代，
         * BCE 前一个时代，时代倒退。
         * CE 当前正在用的时代从
         */
        ChronoField era = ChronoField.ERA;
        /**
         * 当前时间到1970年的秒数
         */
        ChronoField instantSeconds = ChronoField.INSTANT_SECONDS;
        /**
         * ZonedDateTime.now() 2019-06-10T11:16:45.970414600+08:00[Asia/Shanghai]
         * 1560136605
         */
        instantSeconds.getFrom(ZonedDateTime.now());
        /**
         * 表示时差，偏移了多少秒
         */
        ChronoField offsetSeconds = ChronoField.OFFSET_SECONDS;
        /**
         * 28800 8H ,东八区
         */
        offsetSeconds.getFrom(OffsetDateTime.now());

    }
}
