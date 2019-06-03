package com.jzz.newspeciality.java8.time.duration;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

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
         * 仅支持 SECONDS  NANOS
         * 0
         */
        duration.get(ChronoUnit.SECONDS);
        /**
         * 获取此持续时间支持的单位集 SECONDS  NANOS
         */
        duration.getUnits();
        /**
         * 检查此持续时间是否为零长度
         */
        duration.isZero();
        /**
         * 检查此持续时间是否为负数
         */
        duration.isNegative();
        /**
         * 使用指定的秒数返回此持续时间的副本。相当于set,返回新实例
         */
        duration.withSeconds(1);
        duration.withNanos(1);
        /**
         * add操作
         */
        duration.plus(Duration.ZERO);
        /**
         * sub操作
         */
        duration.minus(Duration.ZERO);
        /**
         * 乘法操作
         */
        duration.multipliedBy(2);
        /**
         * 除法操作
         */
        duration.dividedBy(2);
        /**
         * 取负数
         */
        duration.negated();
        /**
         * 绝对值操作
         */
        duration.abs();
        /**
         * add操作
         */
        Temporal temporal = duration.addTo(LocalDateTime.now());
        /**
         * 相减操作
         */
        duration.subtractFrom(LocalDateTime.now());
        /**
         * 转为多少天
         */
        duration.toDays();
        /**
         * 55
         */
        Duration.ofHours(55).toHours();
        duration.toHours();
        /**
         * 返回小时部分
         * 7
         */
        Duration.ofHours(55).toHoursPart();
        /**
         * 转其他格式的duration
         */
        duration.truncatedTo(ChronoUnit.SECONDS);
    }
}
