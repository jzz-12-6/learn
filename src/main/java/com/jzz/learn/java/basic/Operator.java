package com.jzz.learn.java.basic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Operator {
    public static void main(String[] args) {
        int a = 10;
        int b = 5;
        // << 按位左移运算符。左操作数按位左移右操作数指定的位数。
        log.info("a<< 1={}" ,a << 1);
        // >>按位右移运算符。左操作数按位右移右操作数指定的位数。
        log.info("a >> 1={}" ,a >> 1);
        //~按位取反运算符翻转操作数的每一位，即0变成1，1变成0。
        log.info("~a={}" ,~a);
        //^如果相对应位值相同，则结果为0，否则为1
        log.info("a ^ b={}" ,a ^ b);
        // | 如果相对应位都是0，则结果为0，否则为1
        log.info("a | b={}" ,a | b);
        // ＆ 如果相对应位都是1，则结果为1，否则为0
        log.info("a & b={}" ,a & b);
    }
}
