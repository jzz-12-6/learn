package com.jzz.newspeciality.java8.time.yearmonthday.yearmonth;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 表示年月
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
        /**
         *
         */
        yearMonth.with(LocalDate.now());
        System.out.println(yearMonth);

    }
}
