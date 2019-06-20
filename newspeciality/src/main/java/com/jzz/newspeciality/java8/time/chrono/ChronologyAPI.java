package com.jzz.newspeciality.java8.time.chrono;

import java.time.*;
import java.time.chrono.*;
import java.time.format.ResolverStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.*;

/**
 * 日历系统，用于组织和识别日期。
 * 比如民国，泰国，日本等
 * 主日期和时间API建立在ISO日历系统上
 * @author jzz
 * @date 2019/6/20
 */
public class ChronologyAPI {
    public static void main(String[] args) {
        //静态方法
        /**
         * 指定日期获取日历系统
         * @param TemporalAccessor 日期
         * ISO
         */
        Chronology from = Chronology.from(LocalDate.now());
        /**
         * 指定区域获取日历系统
         * @param Locale 区域
         */
        Chronology ofLocale = Chronology.ofLocale(Locale.CHINA);
        /**
         * 指定ID获取日历系统
         * @param String ID 如ISO
         */
        Chronology of = Chronology.of(ofLocale.getId());
        /**
         * 返回可用的日历系统
         * Japanese
         * ISO
         * Hijrah-umalqura
         * Minguo
         * ThaiBuddhist
         * ISO
         */
        Set<Chronology> availableChronologies = Chronology.getAvailableChronologies();


        Chronology chronology = MinguoChronology.INSTANCE;
        //接口方法
        /**
         * 获取当前日历系统ID
         * ISO
         */
        String id = chronology.getId();
        /**
         * 获取日历系统的日历类型。
         * iso8601
         */
        String calendarType = chronology.getCalendarType();
        /**
         * 构建日期
         * @param Era 年代
         * @param yearOfEra 年数
         * @param month 月份
         * @param dayOfMonth 天数
         * Minguo ROC 1-01-01
         */
        ChronoLocalDate date = chronology.date(MinguoEra.ROC, 1, 1, 1);
        /**
         * 构建日期
         * @param prolepticYear 年数
         * @param month 月份
         * @param dayOfMonth 天数
         * Minguo ROC 1-01-01
         */
        ChronoLocalDate date1 = chronology.date(1, 1, 1);
        /**
         * 构建日期
         * @param Era 年代
         * @param yearOfEra 年数
         * @param dayOfYear 天数
         * Minguo ROC 1-01-01
         */
        ChronoLocalDate date2 = chronology.dateYearDay(MinguoEra.ROC, 1, 1);
        /**
         * 构建日期
         * @param prolepticYear 年数
         * @param dayOfYear 天数
         * Minguo ROC 1-01-01
         */
        ChronoLocalDate date3 = chronology.dateYearDay(1, 1);
        /**
         * 从1970-01-01开始指定天数构建日期
         * @param epochDay 天数
         * Minguo ROC 59-01-02
         */
        ChronoLocalDate date4 = chronology.dateEpochDay(1);
        /**
         * 获取当前日期
         * Minguo ROC 108-06-20
         */
        ChronoLocalDate now = chronology.dateNow();
        /**
         * 获取当前日期
         * @param Clock 指定时钟
         */
        chronology.dateNow(Clock.systemDefaultZone());
        /**
         * 获取当前日期
         *  @param ZoneId 区域ID
         */
        chronology.dateNow(ZoneId.systemDefault());
        /**
         * 指定时间获取日期
         *  @param TemporalAccessor 日期
         * Minguo ROC 108-06-20
         */
        ChronoLocalDate chronoLocalDate = chronology.date(LocalDate.now());
        /**
         * 指定时间获取日期
         *  @param TemporalAccessor 日期
         * Minguo ROC 108-06-20T16:21:51.470870700
         */
        ChronoLocalDateTime chronoLocalDateTime = chronology.localDateTime(LocalDateTime.now());
        /**
         * 指定时间获取日期
         *  @param TemporalAccessor 日期
         * Minguo ROC 108-06-20T16:38:56.075952400+08:00[Asia/Shanghai]
         */
        ChronoZonedDateTime chronoZonedDateTime = chronology.zonedDateTime(ZonedDateTime.now());
        /**
         * 是否为闰年
         * true
         */
        boolean leapYear = chronology.isLeapYear(1);
        /**
         * 根据时代和年代计算预感年份。
         * 1
         */
        int i = chronology.prolepticYear(MinguoEra.ROC, 1);
        /**
         * 从数值创建年表时代对象。
         * ROC
         */
        Era era = chronology.eraOf(1);
        /**
         * 获取时代列表。
         * BEFORE_ROC
         * ROC
         */
        List<Era> eras = chronology.eras();
        /**
         * 获取指定字段的范围
         * @param ChronoField 字段值
         * -1000001910 - 999998088
         */
        ValueRange range = chronology.range(ChronoField.YEAR);
        /**
         * 获取日历系统的展示
         * @param TextStyle 展示的方式
         * @param Locale 展示的语言
         * 民国纪年
         */
        String displayName = chronology.getDisplayName(TextStyle.FULL, Locale.CHINA);
        Map<TemporalField, Long> fieldValues  = new HashMap<>();
        fieldValues.put(ChronoField.YEAR,1L);
        fieldValues.put(ChronoField.MONTH_OF_YEAR,1L);
        fieldValues.put(ChronoField.DAY_OF_MONTH,1L);
        /**
         * @param Map<TemporalField, Long> 设置的字段
         * @param ResolverStyle 解析方法
         * Minguo ROC 1-01-01
         */
        ChronoLocalDate chronoLocalDate1 = chronology.resolveDate(fieldValues, ResolverStyle.STRICT);
        /**
         * 根据年，月和日获得此年表的时间段。
         * @param years 年份
         * @param months 月份
         * @param days  天数
         * Minguo P1Y1M1D
         */
        ChronoPeriod period = chronology.period(1, 1, 1);
        /**
         * 获取到1970-01-01T00:00:00的秒数
         * -1830409139
         */
        long epochSecond = chronology.epochSecond(1, 1, 1, 1, 1, 1, ZoneOffset.ofHours(8));
        /**
         * 获取到1970-01-01T00:00:00的秒数
         * @param Era 时代
         * @param yearOfEra 年份
         * @param month 月份
         * @param dayOfMonth 天数
         * @param hour 小时  0 to 23
         * @param minute 分钟  0 to 59
         * @param second 秒数  0 to 59
         * @param ZoneOffset  时区
         * -1830409139
         */
        long epochSecond1 = chronology.epochSecond(MinguoEra.ROC, 1, 1, 1, 1, 1, 1, ZoneOffset.ofHours(8));
    }
}
