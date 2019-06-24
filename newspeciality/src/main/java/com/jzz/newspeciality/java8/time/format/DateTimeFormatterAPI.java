package com.jzz.newspeciality.java8.time.format;

import java.text.Format;
import java.text.ParsePosition;
import java.time.*;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.util.Locale;
import java.util.Set;

/**
 * 用于打印和解析日期时间对象的Formatter。
 * @author jzz
 * @date 2019/6/14
 */
public class DateTimeFormatterAPI {
    //私有属性 都有get方法，set方法用with替代，但会返回新对象
    /**
     * 解析器
     */
    //private  CompositePrinterParser printerParser;
    /**
     * 用于格式化的区域
     */
    private  Locale locale;
    /**
     * 用于格式化的符号
     */
    private  DecimalStyle decimalStyle;
    /**
     * 要使用的解析器样式
     */
    private  ResolverStyle resolverStyle;
    /**
     * 用于解析的字段
     */
    private  Set<TemporalField> resolverFields;
    /**
     * 用于格式化的日历
     */
    private  Chronology chrono;
    /**
     * 用于格式化的区域
     */
    private  ZoneId zone;



    public static void main (String[] args) throws Exception{
        /**
         * 字符串构建实例
         * LocalDateTime.now().format(dateTimeFormatter) 2019-06-22
         */
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        /**
         *  @param pattern String 格式
         *  @param Locale 格式化的区域
         */
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA);
        /**
         * 返回ISO年表的特定于语言环境的日期格式。
         * 2019年6月22日星期六
         * @param dateStyle 日期格式
         */
        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        /**
         * 返回ISO年表的特定于语言环境的时间格式。
         * 中国标准时间 下午5:09:36
         * @param timeStyle 时间格式
         */
        DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL);
        /**
         * 返回ISO年表的特定于语言环境的日期时间格式化程序。
         * 2019年6月22日星期六 中国标准时间 下午5:11:26
         * @param dateTimeStyle 日期时间格式
         */
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
        /**
         * 返回ISO年表的特定于语言环境的日期和时间格式
         * @param dateStyle 日期格式
         * @param timeStyle 时间格式
         * 2019年6月24日星期一 中国标准时间 上午11:26:50
         */
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL,FormatStyle.FULL);
        //自带格式
        /**
         * ISO日期格式化程序，用于格式化或解析没有偏移量的日期
         * 例如“2011-12-03”
         */
        DateTimeFormatter isoLocalDate = DateTimeFormatter.ISO_LOCAL_DATE;
        /**
         * ISO日期格式化程序，使用*偏移量格式化或解析日期
         * 例如“2011-12-03 + 01:00”
         */
        DateTimeFormatter isoOffsetDate = DateTimeFormatter.ISO_OFFSET_DATE;
        /**
         * ISO日期格式化程序，使用offset（如果可用）格式化或解析日期，
         * 例如“2011-12-03”或“2011-12-03 + 01:00
         */
        DateTimeFormatter isoDate = DateTimeFormatter.ISO_DATE;
        /**
         * ISO时间格式化程序，用于格式化或解析没有偏移量的时间，
         * 例如“10:15”或“10:15:30
         */
        DateTimeFormatter isoLocalTime = DateTimeFormatter.ISO_LOCAL_TIME;
        /**
         * ISO时间格式化程序，使用偏移格式化或解析时间，
         * 例如'10：15 + 01:00'或'10：15：30 + 01:00'。
         */
        DateTimeFormatter isoOffsetTime = DateTimeFormatter.ISO_OFFSET_TIME;
        /**
         * ISO时间格式化程序，使用offset（如果可用）格式化或解析时间，
         * 例如“2011-12-03”或“2011-12-03 + 01:00
         */
        DateTimeFormatter isoTime = DateTimeFormatter.ISO_TIME;
        /**
         * ISO日期时间格式化程序，用于格式化或解析没有偏移的日期时间，
         * 例如“2011-12-03T10：15：30”。
         */
        DateTimeFormatter isoLocalDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        /**
         * ISO日期时间格式器，用偏移格式化或解析日期时间，
         * 例如'2011-12-03T10：15：30 + 01:00'
         */
        DateTimeFormatter isoOffsetDateTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        /**
         * ISO的日期时间格式化程序，使用偏移量和区域（如果可用）格式化或解析日期时间，
         * 例如“2011-12-03T10:15:30”，2011-12-03T10:15:30 +01:00'或'2011-12-03T10:15:30+01:00[Europe/Paris]'.'。
         */
        DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_DATE_TIME;
        /**
         * 类似ISO日期时间格式化程序，用offset和zone格式化或解析日期时间，
         * 例如'2011-12-03T10：15：30 + 01:00 [Europe / Paris]'。
         */
        DateTimeFormatter isoZonedDateTime = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        /**
         * ISO日期格式化程序，用于格式化或解析序数日期，不带偏移量，
         * 例如“2012-337”。
         */
        DateTimeFormatter isoOrdinalDate = DateTimeFormatter.ISO_ORDINAL_DATE;
        /**
         * ISO日期格式化程序，用于格式化或解析基于周的日期而不使用偏移量，
         * 例如“2012-W48-6”。
         */
        DateTimeFormatter isoWeekDate = DateTimeFormatter.ISO_WEEK_DATE;
        /**
         * ISO即时格式化程序，用于格式化或解析UTC中的瞬间
         * 例如'2011-12-03T10：15：30Z'
         */
        DateTimeFormatter isoInstant = DateTimeFormatter.ISO_INSTANT;
        /**
         * ISO日期格式化程序，用于格式化或解析没有偏移量的日期，
         * 例如“20111203”。
         */
        DateTimeFormatter basicIsoDate = DateTimeFormatter.BASIC_ISO_DATE;
        /**
         * RFC-1123日期时间格式化程序
         * 例如Tue, 3 Jun 2008 11:05:30 GMT
         */
        DateTimeFormatter rfc1123DateTime = DateTimeFormatter.RFC_1123_DATE_TIME;

        /**
         * 一个查询，提供对已解析的超出天数的访问权限
         * 例如24:00 返回Period.ofDays(1)
         *
         */
        TemporalQuery<Period> periodTemporalQuery = DateTimeFormatter.parsedExcessDays();
        /**
         *一个查询，提供对是否解析闰秒的访问权限。
          */
        TemporalQuery<Boolean> booleanTemporalQuery = DateTimeFormatter.parsedLeapSecond();

        /**
         * 使用此格式化程序格式化日期时间对象。
         * @param TemporalAccessor 日期时间对象
         * 2019-06-24
         * 同TemporalAccessor::format
         */
        String format = isoLocalDate.format(LocalDateTime.now());
        /**
         * 使用此格式化程序格式化日期时间对象。将结果输出到指定对象
         * @param TemporalAccessor 日期时间对象
         * @param Appendable 接收结果的对象
         * 2019-06-24
         */
        StringBuilder buf = new StringBuilder(32);
        isoLocalDate.formatTo(LocalDateTime.now(),buf);
        /**
         * 字符串转时间对象
         * @param CharSequence 转换的字符串
         */
        TemporalAccessor parse = isoLocalDate.parse("2019-06-24");
        /**
         * 字符串转时间对象，从指定的位置开始解析
         * @param CharSequence 转换的字符串
         * @param ParsePosition 指定的位置 字符串下标
         * {},ISO resolved to 2019-06-24
         */
        TemporalAccessor parse1 = isoLocalDate.parse("12019-06-24", new ParsePosition(1));
        /**
         * 字符串转查询时间对象
         * @param CharSequence 转换的字符串
         * @param TemporalQuery 查询时间对象
         */
        LocalDate parse2 = isoLocalDate.parse("2019-06-24", TemporalQueries.localDate());
        /**
         * 字符串转查询时间对象
         * @param CharSequence 转换的字符串
         * @param TemporalQuery.. 多个查询时间对象，按顺序指定查询，从最佳匹配的完全解析选项开始
         */
        TemporalAccessor temporalAccessor = isoLocalDate.parseBest("2019-06-24", TemporalQueries.localDate(), TemporalQueries.precision());
        /**
         * 字符串转时间对象，从指定的位置开始解析
         * @param CharSequence 转换的字符串
         * @param ParsePosition 指定的位置 字符串下标
         * {MonthOfYear=6, Year=2019, DayOfMonth=24},null
         */
        TemporalAccessor temporalAccessor1 = isoLocalDate.parseUnresolved("12019-06-24", new ParsePosition(1));
        /**
         * 格式化程序转Format
         */
        Format format1 = isoLocalDate.toFormat();
        /**
         * 指定的查询的格式化程序转Format
         * @param TemporalQuery 查询时间对象
         */
        Format format2 = isoLocalDate.toFormat(TemporalQueries.precision());
    }
}
