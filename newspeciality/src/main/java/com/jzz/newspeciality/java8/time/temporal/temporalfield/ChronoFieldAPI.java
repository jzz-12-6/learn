package com.jzz.newspeciality.java8.time.temporal.temporalfield;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * @author jzz
 * @date 2019/6/10
 */
public class ChronoFieldAPI {
    private static  ChronoField CHRONO_FIELD = ChronoField.DAY_OF_YEAR;

    /**
     * 实现的接口方法
     */
    public static void parentMethod(){
        /**
         * 获取请求的语言环境中字段的显示名称
         */
        CHRONO_FIELD.getDisplayName(Locale.CHINA);
        /**
         * 获取测量字段的单位。
         * 该字段的单位是在该范围内变化的周期。
         * Days
         */
        CHRONO_FIELD.getBaseUnit();
        /**
         * 获取字段绑定的范围
         * Weeks
         */
        CHRONO_FIELD.getRangeUnit();
        /**
         * 获取该字段的有效值范围。
         * 1-7
         */
        CHRONO_FIELD.range();
        /**
         * 检查此字段是否表示日期的组成部分。
         * 只要是基于ChronoField#EPOCH_DAY EPOCH_DAY类都为true
         * true
         */
        CHRONO_FIELD.isDateBased();
        /**
         * 检查此字段是否表示时间的组成部分。
         * 只要是基于ChronoField##NANO_OF_DAY NANO_OF_DAY类都为true
         * false
         */
        CHRONO_FIELD.isTimeBased();
        /**
         * 检查时态对象是否支持此字段。
         * @param temporal 时态对象
         * true
         */
        CHRONO_FIELD.isSupportedBy(LocalDate.now());
        /**
         * 使用时态对象获取此字段的有效值范围
         * 1-365
         */
        CHRONO_FIELD.rangeRefinedBy(LocalDate.now());
        /**
         * 从指定的时态对象获取此字段的值
         * LocalDate.now() 2019-06-06
         * 157 第157天
         */
        CHRONO_FIELD.getFrom(LocalDate.now());
        /**
         * 返回指定时态对象的副本，其值为此字段集。
         * 相当于set，但是返回新对象
         */
        LocalDate localDate = CHRONO_FIELD.adjustInto(LocalDate.now(), 1);
    }

    /**
     * 自有方法
     * @param args
     */
    public static void main(String[] args) {
        /**
         * 检查给定值是否在范围内
         * 返回值为 long
         */
        CHRONO_FIELD.checkValidValue(1L);
        /**
         * 检查给定值是否在范围内
         * 返回值为int
         */
        CHRONO_FIELD.checkValidIntValue(1L);
    }
}
