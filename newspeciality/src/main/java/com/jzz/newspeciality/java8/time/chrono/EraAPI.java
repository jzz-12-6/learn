package com.jzz.newspeciality.java8.time.chrono;

import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.format.TextStyle;
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
        System.out.println(displayName);
    }
}
