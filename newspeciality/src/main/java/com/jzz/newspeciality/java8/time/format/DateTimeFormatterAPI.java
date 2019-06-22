package com.jzz.newspeciality.java8.time.format;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalField;
import java.util.Locale;
import java.util.Set;

/**
 * 用于打印和解析日期时间对象的Formatter。
 * @author jzz
 * @date 2019/6/14
 */
public class DateTimeFormatterAPI {
    //私有属性
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
         */
        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        /**
         * 返回ISO年表的特定于语言环境的时间格式。
         * 中国标准时间 下午5:09:36
         */
        DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL);
        /**
         * 返回ISO年表的特定于语言环境的日期时间格式化程序。
         * 2019年6月22日星期六 中国标准时间 下午5:11:26
         */
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
        System.out.println(ZonedDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)));

    }
}
