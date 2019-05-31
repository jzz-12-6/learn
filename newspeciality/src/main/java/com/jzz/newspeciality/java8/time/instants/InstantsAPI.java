package com.jzz.newspeciality.java8.time.instants;

import java.time.*;
import java.time.temporal.*;

/**
 * @author jzz
 * @date 2019/5/31
 */
public class InstantsAPI {
    public static void main(String[] args) throws Exception{
        //创建瞬间

        //最大瞬间 1000000000-12-31T23:59:59.999999999Z
        Instant max = Instant.MAX;
        //最小瞬间 -1000000000-01-01T00:00Z
        Instant min = Instant.MIN;
        //使用系统默认时钟获取当前时刻 2019-05-31T03:22:34.022633200Z
        Instant now = Instant.now();
        //指定时钟获取当前时刻
        Instant.now(Clock.systemUTC());
        /**
         * 从纪元开始（1970-01-01T00:00:00Z）的指定秒数创建时刻
         * 1970-01-01T00:00:09Z
         */
        Instant.ofEpochSecond(9);
        /**
         * @param epochSecond 指定秒数
         * @param nanoAdjustment 偏移量 纳秒 正数或者负数
         * 970-01-01T00:00:09.000000005Z
         */
        Instant.ofEpochSecond(9,5);
        /**
         * 同上
         * 参数为毫秒
         * 1970-01-01T00:00:00.100Z
         */
        Instant.ofEpochMilli(100);
        /**
         * 指定类型转换为Instant
         * @param temporal 转换的时间 必须包含 INSTANT_SECONDS 和OFFSET_SECONDS
         * 2019-05-31T11:54:32.478166300+08:00
         * 2019-05-31T03:54:32.479667400Z
         */
        Instant.from(OffsetDateTime.now());
        /**
         * 字符串转Instant
         *
         */
        Instant.parse("2019-05-31T03:54:32.479667400Z");
        /**
         * 是否支持给定字段
         * true
         */
        now.isSupported(ChronoField.INSTANT_SECONDS);
        /**
         * 指定时间的范围
         * 1 - 31
         */
        now.range(ChronoField.INSTANT_SECONDS);
        /**
         * 获取指定字段的值
         */
        now.get(ChronoField.MILLI_OF_SECOND);
        now.getLong(ChronoField.INSTANT_SECONDS);
        /**
         *返回1970到现在时间的毫秒数
         */
        now.getEpochSecond();
        /**
         *从第二个开始的时间线获取纳米秒数,不会超过999,999,999
         */
        now.getNano();
        /**
         * 调整时间
         * now 2019-05-31T07:02:51.984972400Z
         * Instant.now() 2019-05-31T07:02:52.188116500Z
         */
        now.with(Instant.now());
        /**
         * 设置指定位置的指定值
         * now 2019-05-31T07:02:51.984972400Z
         *     2019-05-31T07:02:51.000000020Z
         */
        now.with(ChronoField.NANO_OF_SECOND,20);
        /**
         * 指定位置截断时间
         * now 2019-05-31T07:00:10.479333100Z
         * 截断之后 2019-05-31T07:00:00Z
         */
        now.truncatedTo(ChronoUnit.HOURS);
        /**
         * 增加指定时间
         */
        now.plus(Duration.ofHours(5));
        /**
         * 指定字段增加指定时间
         */
        now.plus(1,ChronoUnit.DAYS);
        /**
         * 增加指定秒数
         */
        now.plusSeconds(1);
        /**
         * 增加指定毫秒数
         */
        now.plusMillis(1);
        /**
         * 增加指定纳秒数
         */
        now.plusNanos(1);
        /**
         * 减去指定时间 参数同plus
         */
        now.minus(Duration.ofMinutes(10));
        now.minus(1,ChronoUnit.DAYS);
        now.minusMillis(1);
        now.minusNanos(1);
        now.minusSeconds(1);
        /**
         * @param query TemporalQuery
         * 使用指定的查询查询此瞬间
         */
        LocalDate query = now.query(TemporalQueries.localDate());
        /**
         * 调整时间 同with方法
         */
        now.adjustInto(OffsetDateTime.now());
        /**
         * 根据指定的单位计算到另一个瞬间的时间量。
         */
        now.until(Instant.now(),ChronoUnit.SECONDS);
        /**
         *将此瞬间与偏移量组合在一起以创建OffsetDateTime
         * now 2019-05-31T08:46:13.122259500Z
         *     2019-05-31T16:46:13.122259500+08:00
         */
        now.atOffset(ZoneOffset.ofHours(8));
        /**
         * 将此瞬间与时区组合以创建ZonedDateTime。
         * now 2019-05-31T08:52:41.272962200Z
         *     2019-05-31T13:52:41.272962200+05:00
         */
        now.atZone(ZoneOffset.ofHours(5));
        System.out.println(now);
        System.out.println( now.atZone(ZoneOffset.ofHours(5)));
    }
}
