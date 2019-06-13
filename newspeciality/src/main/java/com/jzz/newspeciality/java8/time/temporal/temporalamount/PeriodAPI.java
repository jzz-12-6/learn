package com.jzz.newspeciality.java8.time.temporal.temporalamount;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * 基于日期的时间量，以年，月，日为单位
 * 比如2年，3个月和4天
 * @author jzz
 * @date 2019/6/12
 */
public final class PeriodAPI {
    //三个私有属性
    /**
     * The number of years.
     */
    private  int years ;
    /**
     * The number of months.
     */
    private  int months;
    /**
     * The number of days.
     */
    private  int days;
    public static void main(String[] args) {
        /**
         * 指定年月日
         */
        Period period = Period.of(1, 1, 1);
        /**
         * 指定年
         * 月，日默认为0
         */
        Period.ofYears(1);
        /**
         * 指定月
         * 年，日默认为0
         */
        Period.ofMonths(1);
        /**
         * 指定天
         * 年，月默认为0
         */
        Period.ofDays(1);
        /**
         * 指定周数
         * 年，月默认为0 天数为7*周数
         */
        Period.ofWeeks(1);
        /**
         * 其他类转换
         */
        Period.from(Period.ofDays(1));
        /**
         * Period.ofYears(2)
         */
        Period.parse("P2Y");
        /**
         * 指定两个日期的差值
         */
        Period.between(LocalDate.now(),LocalDate.now().plusDays(1));
        /**
         * 返回指定单位的值
         */
        period.get(ChronoUnit.YEARS);
        /**
         * 返回支持的单位
         */
        period.getUnits();
        period.getYears();
        period.getDays();
        period.getDays();
        /**
         * 获取此期间的年表，即ISO日历系统。
         */
        period.getChronology();
        /**
         * 判断是否为0
         */
        period.isZero();
        /**
         * 判断是否为负
         */
        period.isNegative();
        /**
         * 重新赋值，返回新对象
         */
        period.withDays(1);
        period.withYears(1);
        period.withMonths(1);
        /**
         * 添加，返回新对象
         */
        period.plus(Period.ofDays(1));
        period.plusDays(1);
        period.plusMonths(1);
        period.plusYears(1);
        /**
         * 相减，返回新对象
         */
        period.minus(Period.ofDays(1));
        period.minusDays(1);
        period.minusMonths(1);
        period.minusYears(1);
        /**
         * 乘法，返回新对象
         */
        period.multipliedBy(3);
        /**
         * 取反
         */
        period.negated();
        /**
         * 将月份转成年
         * 1 Year and 15 months 转为 2 years and 3 months
         */
        period.normalized();
        /**
         * 转为多少月
          */
        period.toTotalMonths();
        /**
         * 相加
         */
        period.addTo(LocalDate.now());
        /**
         * 相减
         */
        period.subtractFrom(LocalDate.now());
    }
}
