package com.jzz.learn.designpatterns.behavioral.interpreter;

/**
 * 抽象表达式类
 * @author jzz
 * @date 2019-5-16
 */
public interface Expression {
    boolean interpret(String context);
}
