package com.jzz.newspeciality.java8.time.temporal.temporalunit;

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
        System.out.println( temporalUnit.isDurationEstimated());
        System.out.println(ChronoUnit.YEARS.isDurationEstimated());
    }
}
