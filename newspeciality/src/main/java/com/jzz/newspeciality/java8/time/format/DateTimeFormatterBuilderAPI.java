package com.jzz.newspeciality.java8.time.format;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.*;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Locale;

import static java.time.temporal.ChronoField.*;

/**
 * 用于创建日期时格格式器。
 * @author jzz
 * @date 2019/6/25
 */
public class DateTimeFormatterBuilderAPI {
    //私有属性
    /**
     * 当前活动的构建器
     */
    private DateTimeFormatterBuilder active ;
    /**
     * 父构建器，对于最外层构建器为null
     */
    private  DateTimeFormatterBuilder parent;
    /**
     * 将使用的构建器列表
     */
    //private final List<DateTimeFormatterBuilder.DateTimePrinterParser> printerParsers = new ArrayList<>();
    /**
     * 此构建器是否生成可选的格式化程序.
     */
    private boolean optional;
    /**
     * 填充下一个字段的宽度。
     */
    private int padNextWidth;
    /**
     * 填充下一个字段的字符。
     */
    private char padNextChar;
    /**
     * 最后一个可变宽度值解析器的索引。
     */
    private int valueParserIndex = -1;


    public static void main(String[] args) {
        /**
         * 获取区域设置和年表的日期和时间样式的格式设置模式。
         * @param dateStyle FormatStyle 日期格式
         * @param timeStyle FormatStyle 时间格式
         * @param chrono Chronology 日历系统
         * @param locale Locale 区域
         * y年M月d日EEEE zzzz ah:mm:ss
         */
        String localizedDateTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.FULL, FormatStyle.FULL, Chronology.ofLocale(Locale.getDefault()), Locale.getDefault());
        String s= LocalDateTime.now().toString();
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        /**
         * 将解析样式更改为格式化程序的其余部分区分大小写。
         * 解析可以区分大小写或不区分大小写 - 默认情况下区分大小写
         */
        DateTimeFormatterBuilder parseCaseSensitive = builder.parseCaseSensitive();
        /**
         * 将解析样式更改为格式化程序的其余部分严格。
         * 解析可以是严格的或宽松的 - 默认情况下严格。
         */
        DateTimeFormatterBuilder parseStrict = builder.parseStrict();
        /**
         * 将解析样式更改为格式化程序的其余部分是宽松的。
         */
        DateTimeFormatterBuilder parseLenient = builder.parseLenient();
        /**
         * 将字段的默认值附加到格式化程序以用于解析
         * @param field TemporalField 指定的日期字段
         * @param value long 指定的值
         */
        DateTimeFormatterBuilder parseDefaulting = parseLenient.parseDefaulting(YEAR, 1L);
        System.out.println(LocalDateTime.now().format(new DateTimeFormatterBuilder()
                .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                .appendValue(MONTH_OF_YEAR, 2)
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral('-')
                .appendValue(HOUR_OF_DAY, 2)
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendValue(SECOND_OF_MINUTE).parseDefaulting(YEAR, 1L).toFormatter()));
    }

    /**
     * 自定义格式
     * LocalDateTime.now() 2019-06-27T17:32:56.017721800
     * FORMATTER 20190627-173256
     */
    public static DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('-')
            .appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendValue(SECOND_OF_MINUTE)
            .toFormatter(Locale.getDefault());
}
