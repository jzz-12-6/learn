package com.jzz.newspeciality.java8.time.duration;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * @author jzz
 * @date 2019/5/31
 */
public class DurationAPI {
    public static void main(String[] args) {
        /**
         * 返回两个时间的时间跨度
         * 格式 PTnHnMnS
         */
        /**
         * PT0S
         */
        Duration duration = Duration.ZERO;
        /**
         * PT120H
         */
        Duration.ofDays(5);
        Duration.ofHours(5);
        Duration.ofMinutes(5);
        Duration.ofSeconds(6);
        /**
         * 指定秒和纳秒
         * PT1.000000001S
         */
        Duration.ofSeconds(1,1);
        Duration.ofMillis(1);
        Duration.ofNanos(1);
        /**
         * 指定时间和单位
         */
        Duration.of(1, ChronoUnit.MILLIS);
        /**
         * 从时间量获取Duration的实例
         * PT24H
         */
        Duration.from(ChronoUnit.DAYS.getDuration());
        /**
         * 字符串转Duration的实例
         */
        Duration.parse("PT24H");
        /**
         * 获得表示两个时间对象之间的持续时间的持续时间
         * 时间必须包含秒数
         * PT5H54M20.7739296S
         */
        Duration.between(LocalTime.NOON,LocalTime.now());
        /**
         * 获取所请求单元的值
         */
        duration.get(ChronoUnit.SECONDS);
        System.out.println( duration.get(ChronoUnit.DAYS));
        Duration.between(LocalTime.now(),LocalTime.now());
    }
}
