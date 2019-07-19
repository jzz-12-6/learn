package com.jzz.newspeciality.java8.time.localdate;

import java.time.*;

/**
 * instance method
 * @author jzz
 * @date 2019/5/31
 */
public class LocalDateAPI {
    public static void main(String[] args) {

        //最大时间 +999999999-12-31
        LocalDate maxDate = LocalDate.MAX;
        //最小时间 -999999999-01-01
        LocalDate minDate = LocalDate.MIN;
        //初始时间 1970-01-01
        LocalDate epochDate = LocalDate.EPOCH;
        //获取当前时间 2019-05-31
        LocalDate now = LocalDate.now();
        /**
         * 获取当前日期
         * @param ZoneId 区域ID
         */
        LocalDate now1 = LocalDate.now(ZoneId.systemDefault());
        /**
         * 获取当前日期
         * @param Clock 时钟
         */
        LocalDate now2 = LocalDate.now(Clock.systemDefaultZone());
        /**
         * 构建日期
         * @param year 年
         * @param month 月
         * @param dayOfMonth 日
         * 0001-01-01
         */
        LocalDate of = LocalDate.of(1, 1, 1);
        LocalDate.of(1, Month.JANUARY,1);
        /**
         * 构建日期
         * @param year 年
         * @param dayOfYear 一年中的天数
         * 0001-01-01
         */
        LocalDate ofYearDay = LocalDate.ofYearDay(1, 1);
        /**
         * 构建日期
         * @param instant Instant 瞬时
         * @param zone   ZoneId 区域ID
         */
        LocalDate ofInstant = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
        /**
         * 从1970-01-01起指定天数构建日期
         *  @param epochDay long 天数
         */
        LocalDate ofEpochDay = LocalDate.ofEpochDay(1);
        /**
         * 其他日期获取LocalDate
         * @param temporal TemporalAccessor 日期
         */
        LocalDate from = LocalDate.from(LocalDateTime.now());
        System.out.println(ofEpochDay);
    }
}
