package com.jzz.newspeciality.java8.time.temporal.temporalunit;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * 日期单位 比如秒，年，月
 * @author jzz
 * @date 2019/6/10
 */
public class TemporalUnitAPI {
    public static void main(String[] args) {
        TemporalUnit temporalUnit = ChronoUnit.DAYS;
        /**
         * 获取单位的范围 可能是估计值
         * PT24H
         */
        temporalUnit.getDuration();
        /**
         * 检查单位的持续时间是否为估计值。
         * true
         */
        temporalUnit.isDurationEstimated();
        /**
         * 检查此单位是否代表日期的组成部分。
         * true
         */
        temporalUnit.isDateBased();
        /**
         * 检查此单位是否代表某个时间的组成部分。
         * false
         */
        temporalUnit.isTimeBased();
        /**
         * 检查指定的时态对象是否支持此单位。
         * true
         */
        temporalUnit.isSupportedBy(LocalDate.now());
        /**
         * 返回指定时间段的指定时间对象的副本。
         * LocalDate.now() 2019-06-10
         * 2019-06-15
         */
        LocalDate localDate = temporalUnit.addTo(LocalDate.now(), 5);
        /**
         * 指定日期相差多久
         * 1
         */
        temporalUnit.between(LocalDate.now(),LocalDate.now().plusDays(1));
    }
}
