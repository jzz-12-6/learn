package com.jzz.newspeciality.java8.time.format;

import java.util.Optional;

/**
 *
 * @author jzz
 * @date 2019/6/14
 */
public class DateTimeFormatterAPI {
    public static void main (String[] args) throws Exception{
       Optional.ofNullable("1").filter(String::isBlank).orElseThrow(()->new RuntimeException("1"));
    }
}
