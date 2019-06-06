package com.jzz.newspeciality.java8.time.temporal;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Locale;

/**
 * 日期时间字段（如一年的某天，一周的某天）
 * 常用单位定义在 {@link ChronoField}.
 * 此类是不可变，且线程安全的
 * @author jzz
 * @date 2019/6/6
 */
public class TemporalFieldAPI {

    public static void main(String[] args) {
        TemporalField temporalField = ChronoField.DAY_OF_YEAR;
        /**
         * 获取请求的语言环境中字段的显示名称
         */
        temporalField.getDisplayName(Locale.CHINA);
        /**
         * 获取测量字段的单位。
         * 该字段的单位是在该范围内变化的周期。
         * Days
         */
        temporalField.getBaseUnit();
        /**
         * 获取字段绑定的范围
         * Weeks
         */
       temporalField.getRangeUnit();
        /**
         * 获取该字段的有效值范围。
         * 1-7
         */
        temporalField.range();
        /**
         * 检查此字段是否表示日期的组成部分。
         * 只要是基于ChronoField#EPOCH_DAY EPOCH_DAY类都为true
         * true
         */
        temporalField.isDateBased();
        /**
         * 检查此字段是否表示时间的组成部分。
         * 只要是基于ChronoField##NANO_OF_DAY NANO_OF_DAY类都为true
         * false
         */
        temporalField.isTimeBased();
        /**
         * 检查时态对象是否支持此字段。
         * @param temporal 时态对象
         * true
         */
        temporalField.isSupportedBy(LocalDate.now());
        /**
         * 使用时态对象获取此字段的有效值范围
         * 1-365
         */
        System.out.println(temporalField.rangeRefinedBy(LocalDate.now()));
        temporalField.rangeRefinedBy(LocalDate.now());
        /**
         * 从指定的时态对象获取此字段的值
         * LocalDate.now() 2019-06-06
         * 157 第157天
         */
        temporalField.getFrom(LocalDate.now());
        /**
         * 返回指定时态对象的副本，其值为此字段集。
         * 相当于set，但是返回新对象
         */
        LocalDate localDate = temporalField.adjustInto(LocalDate.now(), 1);
        System.out.println(localDate);
    }
}
