package com.jzz.newspeciality.java8.time.yearmonthday.year;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import static java.time.temporal.ChronoUnit.YEARS;

/**
 * 表示具有时区规则的日期时年
 * @author jzz
 * @date 2019/6/4
 */
public class YearAPI {
    public static void main(String[] args) {
        // CREATE
        /**
         * 当前年
         * 2019
         */
        Year year = Year.now();
        Year.now(ZoneId.systemDefault());
        Year.now(Clock.systemDefaultZone());
        /**
         * 范围-999_999_999-999_999_999
         */
        Year.of(2019);
        /**
         * 从时态对象获得实例
         */
        Year.from(LocalDate.now());
        Year.parse("2019");
        DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy");
        Year.parse("2019", d);


        //API
        /**'
         * 是否为闰年
         */
        Year.now().isLeap();
        /**
         * 返回当前年份
         */
        year.getValue();
        /**
         * 是否支持指定字段
         */
        year.isSupported(YEAR);
        year.isSupported(YEARS);
        /**
         * 查询指定字段范围
         * 1 - 999999999
         */
        year.range(YEAR_OF_ERA);
        /**
         * 返回指定字段的值
         */
        year.get(YEAR_OF_ERA);
        year.getLong(YEAR_OF_ERA);
        /**
         * 检查月份是否对今年有效
         */
        year.isValidMonthDay(MonthDay.now());
        /**
         * 调整副本
         */
        year.with(Year.now());
        year.with(YEAR,2019);
        year.adjustInto(LocalDate.now());
        /**
         * 增加
         */
        year.plus(Period.ofYears(1));
        year.plus(1, YEARS);
        year.plusYears(1);
        /**
         * 减去
         */
        year.minus(Period.ofYears(1));
        year.minus(1, YEARS);
        year.minusYears(1);
        /**
         * 计算差值
         */
        year.until(Year.now(), YEARS);
        /**
         * 转localdate 值为366
         * 2019-01-01
         */
        year.atDay(1);
        /**
         * 转YearMonth
         * 2019-01
         */
        year.atMonth(1);
        year.atMonth(Month.JANUARY);
        year.atMonthDay(MonthDay.now());
        /**
         * 检查今年是否在指定年份之后
         */
        year.isAfter(Year.now());
        year.isBefore(Year.now());
    }
}
