package com.jzz.newspeciality.java8.time.temporal;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

/**
 * 定义对时态对象的读写访问的框架级接口
 * 这是日期，时间和偏移对象的基本接口类型
 * 足够完整，可以使用加号和减号进行操作。
 * @author jzz
 * @date 2019/6/18
 */
public class TemporalAPI {
    public static void main(String[] args) {
        Temporal temporal = LocalDateTime.now();
        /**
         * 是否支持指定字段
         */
        temporal.isSupported(ChronoUnit.HOURS);
        /**
         * 调整日期
         */
        temporal.with(LocalDateTime.now());
        /**
         * 指定字段调整日期
         */
        temporal.with(ChronoField.DAY_OF_WEEK,5);
        /**
         * 相加
         */
        temporal.plus(Duration.ofDays(5));
        /**
         * 指定字段相加
         */
        temporal.plus(6,ChronoUnit.DAYS);
        /**
         * 相减
         */
        temporal.minus(Duration.ofDays(5));
        temporal.minus(6,ChronoUnit.DAYS);
        /**
         * 和指定日期，指定字段对比差值
         */
        temporal.until(LocalDateTime.now(),ChronoUnit.DAYS);
    }
}
