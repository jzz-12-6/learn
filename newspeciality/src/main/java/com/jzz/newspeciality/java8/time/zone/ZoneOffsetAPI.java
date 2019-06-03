package com.jzz.newspeciality.java8.time.zone;

import java.time.ZoneOffset;

/**
 * @author jzz
 * @date 2019/6/3
 */
public class ZoneOffsetAPI {
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

    }
}
