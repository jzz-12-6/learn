package com.jzz.newspeciality.java8.time.zone;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalQueries;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;

/**
 * 表示具有时区规则的日期时间
 * @author jzz
 * @date 2019/6/4
 */
public class ZonedDateTimeAPI {
    public static void main(String[] args) {
        // CREATE
        /**
         * 从默认时区中的系统时钟获取当前日期时间。
         * 2019-06-04T17:14:02.233653400+08:00[Asia/Shanghai]
         */
        ZonedDateTime dateTime = ZonedDateTime.now();
        /**
         * 指定时区获取当前日期时间。
         */
        ZonedDateTime.now(ZoneId.systemDefault());
        /**
         * 从指定的时钟获得当前日期时间
         */
        ZonedDateTime.now(Clock.systemDefaultZone());
        /**
         * 从本地日期和时间获取实例
         * 2019-06-04T17:19:55.246147700+08:00[Asia/Shanghai]
         */
        ZonedDateTime.of(LocalDate.now(), LocalTime.now(),ZoneId.systemDefault());
        ZonedDateTime.of(LocalDateTime.now(),ZoneId.systemDefault());
        /**
         * 2019-01-01T01:01:01.000000001+08:00[Asia/Shanghai]
         */
        ZonedDateTime.of(2019,01,01,01,01,01,01,ZoneId.systemDefault());
        /**
         * 方法使用首选偏移量从本地日期时间获取ZonedDateTime的实例。
         * 如果指定的引用区域偏移无效，则使用重叠的较早区域偏移
         */
        ZonedDateTime.ofLocal(LocalDateTime.now(),ZoneId.systemDefault(),ZoneOffset.UTC);
        /**
         * 从Instant和区域ID获取ZonedDateTime的实例。
         */
        ZonedDateTime.ofInstant(Instant.now(),ZoneId.systemDefault());
        ZonedDateTime.ofInstant(LocalDateTime.now(),ZoneOffset.UTC,ZoneId.systemDefault());
        /**
         * 获取ZonedDateTime的实例,严格验证本地日期时间，偏移量和区域ID的组合。
         */
        ZonedDateTime.ofStrict(LocalDateTime.now(),ZoneOffset.ofHours(8),ZoneId.systemDefault());
        /**
         * 从temporal对象获取ZonedDateTime的实例。
         */
        ZonedDateTime.from(ZonedDateTime.now());
        /**
         * 字符串转ZonedDateTime
         */
        ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]");
        ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]", DateTimeFormatter.ISO_ZONED_DATE_TIME);


        //api
        /**
         * 是否支持指定字段
         */
        dateTime.isSupported(ChronoUnit.SECONDS);
        dateTime.isSupported(ChronoField.DAY_OF_YEAR);
        /**
         * 指定字段的范围
         */
        dateTime.range(INSTANT_SECONDS);
        /**
         * 获取指定字段的值
         */
        dateTime.get(ChronoField.DAY_OF_YEAR);
        dateTime.getLong(INSTANT_SECONDS);
        dateTime.getYear();
        dateTime.getMonthValue();
        dateTime.getMonth();
        dateTime.getDayOfMonth();
        dateTime.getDayOfYear();
        dateTime.getDayOfWeek();
        dateTime.getHour();
        dateTime.getMinute();
        dateTime.getSecond();
        dateTime.getNano();
        /**
         * 获取区域偏移量
         */
        dateTime.getOffset();
        /**
         * 获取时区
         */
        dateTime.getZone();
        /**
         * 返回此日期时间，将区域偏移更改为本地时间线重叠处的两个有效偏移中较早的一个的副本。
         */
        dateTime.withEarlierOffsetAtOverlap();
        /**
         * 返回此日期时间，将区域偏移更改为本地时间线重叠处的两个有效偏移中的较晚者的副本。
         */
        dateTime.withLaterOffsetAtOverlap();
        /**
         * 使用不同的时区返回此日期时间的副本，如果可能保留本地日期时间。
         */
        dateTime.withZoneSameLocal(ZoneId.systemDefault());
        /**
         * 使用不同的时区返回此日期时间保留该瞬间的副本。
         */
        dateTime.withZoneSameInstant(ZoneId.systemDefault());
        /**
         * 返回此日期时间将区域ID设置为偏移量的副本。
         */
        dateTime.withFixedOffsetZone();
        /**
         * 转其他格式
         */
        dateTime.toLocalDate();
        dateTime.toLocalDateTime();
        dateTime.toOffsetDateTime();
        dateTime.toLocalTime();
        /**
         * 返回此日期时间的调整副本
         */
        dateTime.with(LocalDate.now());
        dateTime.with(INSTANT_SECONDS,10);
        dateTime.withYear(2019);
        dateTime.withMonth(1);
        dateTime.withDayOfMonth(1);
        dateTime.withDayOfYear(1);
        dateTime.withHour(1);
        dateTime.withMinute(1);
        dateTime.withSecond(1);
        dateTime.withNano(1);

        /**
         * 返回此ZonedDateTime并截断时间的副本。
         */
        dateTime.truncatedTo(ChronoUnit.DAYS);

        /**
         * 返回此日期时间添加指定的数量的副本
         */
        dateTime.plus(Duration.ofHours(5));
        /**
         * 返回此日期时间减去指定的数量的副本
         */
        dateTime.minus(Duration.ofHours(5));

        /**
         * 使用指定的查询来查询此日期时间。
         * 2019-06-04
         */
        dateTime.query(TemporalQueries.localDate());
        /**
         * 方法根据指定的单位计算到另一个日期时间的时间量。
         * 1
         */
        dateTime.until(ZonedDateTime.now().plusHours(1),ChronoUnit.HOURS);
        /**
         * 转其他格式
         * 18:02:41.455481
         */
        dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
       }
}
