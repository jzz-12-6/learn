package com.jzz.newspeciality.java8.time.zone;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 表示时区ID，例如Europe/Paris
 * @author jzz
 * @date 2019/6/4
 */
public class ZoneIdAPI {
    public static void main(String[] args) {
        /**
         * 区域覆盖的映射，用于启用短时区名称
         */
        Map<String, String> shortIds = ZoneId.SHORT_IDS;
        //create

        /**
         * 获取系统默认时区
         * Asia/Shanghai
         */
        ZoneId zoneId = ZoneId.systemDefault();
        /**
         * 获取可用区域ID的集合
         */
        ZoneId.getAvailableZoneIds();
        /**
         * 从ID获取ZoneId的实例，确保该ID有效且可供使用。
         * "Asia/Shanghai"
         */
        ZoneId.of("Asia/Shanghai");
        /**
         * 使用别名映射使用其ID获取ZoneId的实例以补充标准区域ID。
         * Asia/Shanghai
         */
        ZoneId.of("Asia/Shanghai", new HashMap<>());
        /**
         * 获得包装偏移量的ZoneId实例。
         *  @param prefix 时区ID "GMT", "UTC", or "UT", or ""
         *  @param offset 偏移量
         *  UTC
         */
        ZoneId.ofOffset("UTC", ZoneOffset.UTC);
        /**
         * 从temporal对象获取ZoneId的实例。
         * Asia/Shanghai
         */
        ZoneId.from(ZonedDateTime.now());

        //api
        /**
         * 获取区域的文本表示
         * CT
         */
        zoneId.getDisplayName(TextStyle.SHORT, Locale.CHINA);
        /**
         * 获取此ID的时区规则
         * ZoneRules[currentStandardOffset=+08:00]
         */
        zoneId.getRules();
        /**
         * 获取唯一的时区ID
         * Asia/Shanghai
         */
        zoneId.getId();
        System.out.println(zoneId.getId());

        System.out.println(ZoneId.from(ZonedDateTime.now()));

    }
}
