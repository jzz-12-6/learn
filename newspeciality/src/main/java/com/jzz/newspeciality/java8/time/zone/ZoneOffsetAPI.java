package com.jzz.newspeciality.java8.time.zone;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;

import static java.time.temporal.ChronoField.OFFSET_SECONDS;

/**
 * 类表示格林威治/UTC的时区偏移量
 * @author jzz
 * @date 2019/6/3
 */
public class ZoneOffsetAPI {
    static   ZoneOffset offset = ZoneOffset.ofHours(5);

    public static void main(String[] args) {
        // 创建ZoneOffset
        /**
         * 使用ID获取实例
         * Z
         */
        ZoneOffset.of("Z");
        /**
         * -01:00
         */
        ZoneOffset zoneOffset = ZoneOffset.ofHours(-1);
        /**
         * +01:01
         */
        ZoneOffset.ofHoursMinutes(1,1);
        /**
         * +01:01:01
         */
        ZoneOffset.ofHoursMinutesSeconds(1,1,1);
        /**
         * +00:01:40
         */
        ZoneOffset.ofTotalSeconds(100);
        /**
         * +08:00
         */
        ZoneOffset.from(ZonedDateTime.now());
        /**
         * 18000
         */
        ZoneOffset.ofHours(5).getTotalSeconds();
        /**
         * ZoneRules[currentStandardOffset=+05:00]
         */
        ZoneOffset.ofHours(5).getRules();
        /**
         * 是否支持指定的字段
         * true
         */
        offset.isSupported(ChronoField.OFFSET_SECONDS);
        /**
         * 获取指定字段的有效值范围
         * -64800 - 64800
         */
        offset.range(ChronoField.OFFSET_SECONDS);
        /**
         * 获取指定字段的值
         * 18000
         */
        offset.get(OFFSET_SECONDS);
        offset.getLong(OFFSET_SECONDS);
        /**
         * 调整指定时态对象以具有此ZoneOffset。
         * 2019-06-03T22:13:23.002538800+08:00[Asia/Shanghai]x
         */
        offset.adjustInto(ZonedDateTime.now());
    }
}
