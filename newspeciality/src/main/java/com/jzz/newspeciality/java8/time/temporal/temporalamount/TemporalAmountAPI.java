package com.jzz.newspeciality.java8.time.temporal.temporalamount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;

import static java.time.temporal.ChronoUnit.YEARS;

public class TemporalAmountAPI {
    public static void main(String[] args) {
        TemporalAmount temporalAmount = Period.ofDays(1);
        /**
         * 返回唯一定义此TemporalAmount值的单位列表。
         * [Years, Months, Days]
         */
        List<TemporalUnit> units = temporalAmount.getUnits();
        /**
         * 返回指定单位的值
         * 0
         */
        temporalAmount.get(YEARS);
        /**
         * 相加
         * 等同于plus方法
         */
        temporalAmount.addTo(LocalDate.now());
        /**
         * 相减
         * 等同于minus方法
         */
        temporalAmount.subtractFrom(LocalDate.now());
        System.out.println( temporalAmount.subtractFrom(LocalDate.now()));
        System.out.println( temporalAmount.subtractFrom(LocalDateTime.now()));
        System.out.println(temporalAmount.get(ChronoUnit.DAYS));
    }
}
