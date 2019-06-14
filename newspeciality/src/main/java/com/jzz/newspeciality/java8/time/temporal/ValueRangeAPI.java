package com.jzz.newspeciality.java8.time.temporal;

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
        ValueRange.of(1L,1L);

        ValueRange.of(1,1,1);
        ValueRange.of(1,1,1,1);

    }
}
