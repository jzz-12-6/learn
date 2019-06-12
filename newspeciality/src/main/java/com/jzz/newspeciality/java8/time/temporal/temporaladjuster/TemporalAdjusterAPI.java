package com.jzz.newspeciality.java8.time.temporal.temporaladjuster;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 * 日期调整类
 * @author jzz
 * @date 2019/6/11
 */
public class TemporalAdjusterAPI {
    public static void main(String[] args) {
        /**
         * 2019-06-12
         */
        LocalDate now = LocalDate.now();
        /**
         * 自定义
         * 2019-06-22
         */
        TemporalAdjuster adjuster = TemporalAdjusters.ofDateAdjuster(date -> date.plusDays(10));
        now.with(adjuster);
        /**
         * 调整为当前月第一天
         * 2019-06-01
         */
        now.with(TemporalAdjusters.firstDayOfMonth());
        /**
         * 调整为当前月最后一天
         * 2019-06-30
         */
        now.with(TemporalAdjusters.lastDayOfMonth());
        /**
         * 调整为下个月第一天
         * 2019-07-01
         */
        now.with(TemporalAdjusters.firstDayOfNextMonth());
        /**
         * 调整为当前年第一天
         * 2019-01-01
         */
        now.with(TemporalAdjusters.firstDayOfYear());
        /**
         * 调整为当前年最后一天
         * 2019-12-31
         */
        now.with(TemporalAdjusters.lastDayOfYear());
        /**
         * 调整为下一年最后一天
         * 2020-01-01
         */
        now.with(TemporalAdjusters.firstDayOfNextYear());
        /**
         * 调整为当前月第一个指定星期
         * 2019-06-07
         */
        now.with(TemporalAdjusters.firstInMonth(DayOfWeek.FRIDAY));
        /**
         * 调整为当前月最后一个指定星期
         * 2019-06-28
         */
        now.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));
        /**
         * 调整为指定ordinal的第几个星期
         * ordinal > 0 日期向前调整
         * ordinal < 0 日期向后调整
         * ordinal = 0 返回上个月最后指定星期
         */
        now.with(TemporalAdjusters.dayOfWeekInMonth(1,DayOfWeek.FRIDAY));
        /**
         * 调整为指定下一个星期
         * 2019-06-14
         */
        now.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        /**
         * 调整为指定下一个星期
         * 若指定星期等于该日期星期，返回当前日期
         */
        now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        /**
         * 调整为指定上一个星期
         * 2019-06-07
         */
        now.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        /**
         * 调整为指定上一个星期
         * 若指定星期等于该日期星期，返回当前日期
         */
        now.with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY));

    }
}
