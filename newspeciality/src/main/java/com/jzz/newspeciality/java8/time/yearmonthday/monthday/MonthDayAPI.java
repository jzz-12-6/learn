package com.jzz.newspeciality.java8.time.yearmonthday.monthday;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalQueries;

/**
 * ISO-8601日历系统中的一个月日 比如12-09
 * 实现 TemporalAccessor 日期只读接口
 * 实现 TemporalAdjuster 日期加减接口
 * @author jzz
 * @date 2019/6/19
 */
public class MonthDayAPI {
    //属性
    /**
     * 代表月
     */
    private  int month;
    /**
     * 代表天
     */
    private int day;

    public static void main(String[] args) {
        //Create
        /**
         * 获取当前月日
         * --06-19
         */
        MonthDay monthDay = MonthDay.now();
        MonthDay.now(ZoneId.systemDefault());
        MonthDay.now(Clock.systemDefaultZone());
        /**
         * 指定月日
         */
        MonthDay.of(1,1);
        MonthDay.of(Month.JANUARY,1);
        /**
         * 其他日期转实例
         */
        MonthDay.from(LocalDateTime.now());
        /**
         * 字符串转实例
         */
        MonthDay.parse("--12-01");
        /**
         * 指定转换格式转实例
         */
        DateTimeFormatter d = DateTimeFormatter.ofPattern("MM-dd");
        MonthDay.parse("12-01",d);

        //重载方法
        /**
         * 是否支持字段
         */
        monthDay.isSupported(ChronoField.YEAR_OF_ERA);
        /**
         * 指定字段的范围
         */
        monthDay.range(ChronoField.DAY_OF_MONTH);
        /**
         * 获取指定字段的值
         */
        monthDay.get(ChronoField.DAY_OF_MONTH);
        monthDay.getLong(ChronoField.DAY_OF_MONTH);
        /**
         * 查询
         */
        monthDay.query(TemporalQueries.chronology());
        monthDay.adjustInto(LocalDate.now());


        //自有方法
        /**
         * 获取月份
         */
        monthDay.getMonthValue();
        /**
         * 获取月份枚举
         */
        monthDay.getMonth();
        /**
         * 获取天数
         */
        monthDay.getDayOfMonth();
        /**
         * 检查年份是否对本月有效。
         * 仅在2月29日返回false
         */
        monthDay.isValidYear(2019);
        /**
         * 设置月份
         */
        monthDay.withMonth(1);
        monthDay.with(Month.JANUARY);
        /**
         * 设置天数
         */
        monthDay.withDayOfMonth(1);
        /**
         * 以指定格式输出
         */
        monthDay.format(d);
        /**
         * 指定年返回LocalDate
         */
        LocalDate localDate = monthDay.atYear(2019);
        /**
         * 是否在指定日期之后
         */
        boolean after = monthDay.isAfter(MonthDay.now());
        /**
         * 是否在指定日期之前
         */
        boolean before = monthDay.isBefore(MonthDay.now());
    }
}
