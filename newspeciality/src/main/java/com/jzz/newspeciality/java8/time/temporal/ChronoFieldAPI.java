package com.jzz.newspeciality.java8.time.temporal;

import java.time.temporal.ChronoField;

/**
 * 日期时间字段
 * enum ChronoField implements TemporalField
 * @author jzz
 * @date 2019/6/6
 */
public class ChronoFieldAPI {
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
         * 一天多少小时 范围 0, 11
         */
        ChronoField hourOfDay = ChronoField.HOUR_OF_DAY;
        System.out.println(minuteOfDay.range());
    }
}
