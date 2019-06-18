package com.jzz.newspeciality.java8.time.yearmonthday.month;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalQueries;
import java.util.Locale;

/**
 * 月份枚举
 * 比如 JANUARY
 */
public class MonthAPI {
    public static void main(String[] args) {
        /**
         * 获取枚举值
         * JANUARY  FEBRUARY  MARCH APRIL MAY
         * JUNE JULY AUGUST SEPTEMBER  OCTOBER
         * NOVEMBER DECEMBER
         */
        Month january = Month.JANUARY;
        /**
         * 下标获取月份
         */
        Month month = Month.of(1);
        /**
         * 日期转换月份
         */
        Month.from(LocalDate.now());
        /**
         * 获取枚举下标值
         */
        month.getValue();
        /**
         * 获取枚举展示的名字
         */
        month.getDisplayName(TextStyle.SHORT, Locale.CHINA);

        //接口实现的方法
        /**
         * 是否支持指定字段
         */
        month.isSupported(ChronoField.DAY_OF_YEAR);
        /**
         * 获取指定字段的范围
         */
        month.range(ChronoField.MONTH_OF_YEAR);
        /**
         * 获取指定字段的值
         */
        month.get(ChronoField.MONTH_OF_YEAR);
        month.getLong(ChronoField.MONTH_OF_YEAR);
        /**
         * 指定条件查询
         */
        month.query(TemporalQueries.zone());
        /**
         * 日期相加
         */
        month.adjustInto(LocalDate.now());
        //私有方法
        /**
         * 相加
         */
        month.plus(1);
        /**
         * 相减
         */
        month.minus(1);
        /**
         * 月份的天数
         */
        month.length(true);
        /**
         * 月份的最小天数
         */
        month.minLength();
        /**
         * 月份的最大天数
         */
        month.maxLength();
        /**
         * 当前月份在今年的第一天
         */
        month.firstDayOfYear(false);
        /**
         * 本季度的首月
         */
        month.firstMonthOfQuarter();
    }
}
