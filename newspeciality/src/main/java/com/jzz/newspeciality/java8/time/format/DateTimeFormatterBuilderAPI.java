package com.jzz.newspeciality.java8.time.format;

import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

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
         *
         */
        String localizedDateTimePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.FULL, FormatStyle.FULL, Chronology.ofLocale(Locale.CHINA), Locale.CHINA);
    }
}
