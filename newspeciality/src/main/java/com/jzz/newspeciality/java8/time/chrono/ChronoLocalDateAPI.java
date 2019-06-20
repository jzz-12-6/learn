package com.jzz.newspeciality.java8.time.chrono;

import java.time.chrono.MinguoDate;

/**
 * 日期不包含具体时间 如2012-09-12
 * 用于高级全球化用例
 * 实现 Temporal 日期读写接口
 * 实现 TemporalAdjuster 日期加减接口
 * @author jzz
 * @date 2019/6/20
 */
public class ChronoLocalDateAPI {
    public static void main(String[] args) {
        MinguoDate date = MinguoDate.now();
        System.out.println(date);
    }
}
