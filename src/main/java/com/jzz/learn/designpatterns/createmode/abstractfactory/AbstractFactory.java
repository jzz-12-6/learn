package com.jzz.learn.designpatterns.createmode.abstractfactory;

/**
 * 抽象工厂
 * @author jzz
 * @date 2019/5/21
 */
public abstract class AbstractFactory {
    public abstract Color getColor(String color);
    public abstract Shape getShape(String shape) ;
}
