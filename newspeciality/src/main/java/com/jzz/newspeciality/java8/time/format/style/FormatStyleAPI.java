package com.jzz.newspeciality.java8.time.format.style;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * @author jzz
 * @date 2019/6/22
 */
public class FormatStyleAPI {
    public static void main(String[] args) {
        /**
         * 全部展示
         * 2019年6月22日星期六 中国标准时间 下午5:01:30
         */
        FormatStyle styleFull = FormatStyle.FULL;
        /**
         * 2019年6月22日 CST 下午5:01:30
         */
        FormatStyle styleLong = FormatStyle.LONG;
        /**
         * 2019年6月22日 下午5:01:30
         */
        FormatStyle styleMedium = FormatStyle.MEDIUM;
        /**
         * 短文本展示，通常为数字
         * 2019/6/22 下午5:01
         */
        FormatStyle styleShort = FormatStyle.SHORT;

        System.out.println(ZonedDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(styleFull)));
        System.out.println(ZonedDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(styleLong)));
        System.out.println(ZonedDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(styleMedium)));
        System.out.println(ZonedDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(styleShort)));
    }
}
