package com.jzz.newspeciality.java8.time.temporal.temporalquery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;

/**
 * 查询时态对象的策略
 * 可能是查询日期是否是2月29日前一天的查询
 * 在闰年，或计算下一个生日的天数。
 * 方法等同于TemporalAccessor::query
 * @author jzz
 * @date 2019/6/13
 */
public class TemporalQueryAPI {
    public static void main(String[] args) {
        /**
         * 查询 LocalDate 对象 查询不到返回null
         */
        TemporalQuery<LocalDate> localDateTemporalQuery = TemporalQueries.localDate();
        /**
         * 以下代码等同
         */
        LocalDateTime.now().query(localDateTemporalQuery);
        localDateTemporalQuery.queryFrom(LocalDateTime.now());
        LocalDate.from(LocalDateTime.now());
        /**
         * 查询 LocalTime 对象 查询不到返回null
         */
        TemporalQuery<LocalTime> localTimeTemporalQuery = TemporalQueries.localTime();
        localTimeTemporalQuery.queryFrom(LocalDateTime.now());
        LocalDateTime.now().query(localTimeTemporalQuery);
        LocalTime.from(LocalDateTime.now());
        /**
         * 查询 ZoneId
         */
        TemporalQuery<ZoneId> zoneIdTemporalQuery = TemporalQueries.zoneId();
        TemporalQuery<ZoneId> zone = TemporalQueries.zone();
        /**
         * 查询日历
         */
        TemporalQuery<Chronology> chronology = TemporalQueries.chronology();
        /**
         * 查询最小支持单位
         */
        TemporalQuery<TemporalUnit> precision = TemporalQueries.precision();
        /**
         * Days
         */
        precision.queryFrom(LocalDate.now());
        /**
         * Nanos
         */
        precision.queryFrom(LocalTime.now());

    }
}
