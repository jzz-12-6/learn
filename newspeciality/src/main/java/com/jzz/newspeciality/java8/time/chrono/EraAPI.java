package com.jzz.newspeciality.java8.time.chrono;

import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.format.TextStyle;
import java.time.temporal.*;
import java.util.Locale;

/**
 * 时代
 * 大多数日历系统都有一个纪元，将时间线划分为两个时代。
 * 继承 {@code TemporalAccessor} 日期只读
 * 继承 {@code TemporalAdjuster} 日期调整
 * @author jzz
 * @date 2019/6/20
 */
public class EraAPI {
    public static void main(String[] args) {
        Era era = IsoEra.CE;
        /**
         * 获取时代值
         * 1
         */
        int value = era.getValue();
        /**
         * 获取日历系统的展示
         * @param TextStyle 展示的方式
         * @param Locale 展示的语言
         * 公元
         */
        String displayName = era.getDisplayName(TextStyle.FULL, Locale.CHINA);

        //实现方法
        /**
         * 是否支持指定字段
         * @param TemporalField 时间字段
         * false
         */
        boolean supported = era.isSupported(ChronoField.DAY_OF_YEAR);
        /**
         * 指定字段范围
         * @param TemporalField 时间字段
         * 0 - 1
         */
        ValueRange range = era.range(ChronoField.ERA);
        /**
         * 获取指定字段的值
         * @param TemporalField 时间字段
         */
        int i = era.get(ChronoField.ERA);
        long eraLong = era.getLong(ChronoField.ERA);
        /**
         * 查询指定字段
         * @param TemporalQuery<R> 查询语句
         */
        TemporalUnit query = era.query(TemporalQueries.precision());
        /**
         * 调整日期
         * @param Temporal 日期
         */
        Temporal temporal = era.adjustInto(LocalDateTime.now());
    }
}
