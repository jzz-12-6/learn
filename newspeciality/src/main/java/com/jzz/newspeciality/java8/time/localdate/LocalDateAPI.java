package com.jzz.newspeciality.java8.time.localdate;

import java.time.LocalDate;

/**
 * instance method
 * @author jzz
 * @date 2019/5/31
 */
public class LocalDateAPI {
    public static void main(String[] args) {
        //获取当前时间 2019-05-31
        LocalDate now = LocalDate.now();
        //最大时间 +999999999-12-31
        LocalDate maxDate = LocalDate.MAX;
        //最小时间 -999999999-01-01
        LocalDate minDate = LocalDate.MIN;
        //初始时间 1970-01-01
        LocalDate epochDate = LocalDate.EPOCH;

    }
}
