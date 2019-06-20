package com.jzz.newspeciality.java8.time.yearmonthday.yearmonth;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;

/**
 * 表示年月
 * 实现Temporal 日期读写接口
 * 实现TemporalAdjuster 日期加减接口
 * @author jzz
 * @date 2019/6/5
 */
public class YearMonthAPI {
    public static void main(String[] args) {
        // CREATE
        /**
         * 从默认时区中的系统时钟获取当前年月
         * 2019-06
         */
        YearMonth yearMonth = YearMonth.now();
        YearMonth.now(Clock.systemDefaultZone());
        YearMonth.now(ZoneId.systemDefault());
        /**
         * 指定年月获取实例
         */
        YearMonth.of(2019,6);
        YearMonth.of(2019, Month.JUNE);
        /**
         * 转换
         */
        YearMonth.from(LocalDate.now());
        YearMonth.parse("2019-06");
        DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth.parse("2019-06", d);

        //实现的方法
        /**
         * 是否支持指定字段
         */
        boolean temporalFieldSupported = yearMonth.isSupported(ChronoField.YEAR);
        boolean temporalUnitSupported1 = yearMonth.isSupported(ChronoUnit.DAYS);
        /**
         * 指定字段的范围
         */
        ValueRange range = yearMonth.range(ChronoField.MONTH_OF_YEAR);
        /**
         * 获取指定字段的值
         */
        int i = yearMonth.get(ChronoField.YEAR);
        long aLong = yearMonth.getLong(ChronoField.YEAR);
        /**
         * 调整
         */
        YearMonth with = yearMonth.with(Year.of(2020));
        YearMonth with1 = yearMonth.with(ChronoField.YEAR, 2020);
        /**
         * 相加
         */
        yearMonth.plus(Period.ofYears(1));
        yearMonth.plus(1,ChronoUnit.YEARS);
        /**
         * 相减
         */
        yearMonth.minus(Period.ofYears(1));
        yearMonth.minus(1,ChronoUnit.YEARS);
        /**
         * 查询
         */
        TemporalUnit query = yearMonth.query(TemporalQueries.precision());
        /**
         * 调整，等同于with方法
         */
        yearMonth.adjustInto(YearMonth.now());
        /**
         * 指定时间指定字段的差值
         */
        yearMonth.until(YearMonth.now(),ChronoUnit.YEARS);
        //自有方法
        /**
         * 获取年份
         */
        int year = yearMonth.getYear();
        /**
         * 获取月份
         */
        Month month = yearMonth.getMonth();
        int monthValue = yearMonth.getMonthValue();
        /**
         * 是否为闰年
         */
        boolean leapYear = yearMonth.isLeapYear();
        /**
         * 检查天数是否合法
         */
        boolean validDay = yearMonth.isValidDay(1);
        /**
         * 获取当前月份天数
         */
        int lengthOfMonth = yearMonth.lengthOfMonth();
        /**
         * 获取当前年天数
         */
        int lengthOfYear = yearMonth.lengthOfYear();
        /**
         * 调整年份
         */
        yearMonth.withYear(1);
        /**
         * 调整月份
         */
        yearMonth.withMonth(1);
        /**
         * 相加
         */
        yearMonth.plusMonths(1);
        yearMonth.plusYears(1);
        /**
         * 相减
         */
        yearMonth.minusMonths(1);
        yearMonth.minusYears(1);
        /**
         * 格式化
         */
        String format = yearMonth.format(d);
        /**
         * 指定天数构建LocalDate
         */
        LocalDate localDate = yearMonth.atDay(1);
        /**
         * 该月最后一天构建LocalDate
         */
        LocalDate atEndOfMonth = yearMonth.atEndOfMonth();
        /**
         * 是否在指定日期之后
         */
        yearMonth.isAfter(YearMonth.now());
        /**
         * 是否在指定日期之前
         */
        yearMonth.isBefore(YearMonth.now());
    }
}
