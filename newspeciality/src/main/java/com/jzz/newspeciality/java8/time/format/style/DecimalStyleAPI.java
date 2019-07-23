package com.jzz.newspeciality.java8.time.format.style;

import org.junit.Test;

import java.time.format.DecimalStyle;
import java.util.Locale;
import java.util.Set;

/**
 * 日期和时间格式中使用的本地化十进制样式。
 * @author jzz
 * @date 2019/6/22
 */
public class DecimalStyleAPI {

    /**
     * 标准的非本地化十进制样式符号集。
     * 0 + - .
     */
    @Test
    public void standard(){
        DecimalStyle standard = DecimalStyle.STANDARD;
    }

    /**
     * 列出支持的所有语言环境。
     */
    @Test
    public void getAvailableLocales(){
        Set<Locale> availableLocales = DecimalStyle.getAvailableLocales();
    }

    /**
     * 获得默认样式
     * DecimalStyle[0+-.]
     */
    @Test
    public void ofDefaultLocale(){
        DecimalStyle decimalStyle = DecimalStyle.ofDefaultLocale();
    }

    /**
     * 指定地区获取样式
     * @param locale Locale 地区
     * DecimalStyle[0+-.]
     */
    @Test
    public void of(){
        DecimalStyle of = DecimalStyle.of(Locale.CHINA);
    }
    DecimalStyle decimalStyle = DecimalStyle.of(Locale.CHINA);
    /**
     * 获取属性
     */
    @Test
    public void getAttributes(){
        /**
         * 小数样式
         */
        decimalStyle.getDecimalSeparator();
        /**
         * 负数样式
         */
        decimalStyle.getNegativeSign();
        /**
         * 正数样式
         */
        decimalStyle.getPositiveSign();
        /**
         * 0样式
         */
        decimalStyle.getZeroDigit();
    }

    /**
     * 设置属性值
     */
    @Test
    public void with(){
        decimalStyle.withDecimalSeparator('.');
        decimalStyle.withNegativeSign('+');
        decimalStyle.withPositiveSign('-');
        decimalStyle.withZeroDigit('0');

    }
}
