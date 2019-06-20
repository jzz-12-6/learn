package com.jzz.newspeciality.java8.time.temporal;

import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

/**
 * 日期时间字段的有效值范围。
 * @author jzz
 * @date 2019/6/14
 */
public class ValueRangeAPI {
    /**
     * The smallest minimum value.
     */
    private  long minSmallest;
    /**
     * The largest minimum value.
     */
    private  long minLargest;
    /**
     * The smallest maximum value.
     */
    private  long maxSmallest;
    /**
     * The largest maximum value.
     */
    private  long maxLargest;


    public static void main(String[] args) {
        //CREATE
        ValueRange range = ValueRange.of(1L, 1L);

        ValueRange.of(1,1,1);
        ValueRange.of(1,1,1,1);

        /**
         * 值范围是否固定且完全已知。
         * 比如月份天数可能是28到31天 但年份月数固定为12
         */
        range.isFixed();
        /**
         * 获取属性
         */
        range.getLargestMinimum();
        range.getMaximum();
        range.getMinimum();
        range.getSmallestMaximum();
        /**
         * 范围值是否在int之内
         */
        range.isIntValue();
        /**
         * 给定值是否在范围内
         */
        range.isValidValue(1L);
        /**
         * 给定值是否在范围内且范围值是否在int之内
         */
        range.isValidIntValue(1L);
        /**
         * 给定字段给定值是否在范围内
         */
        range.checkValidValue(1L, ChronoField.DAY_OF_YEAR);
        range.checkValidIntValue(1L, ChronoField.DAY_OF_YEAR);
    }
}
