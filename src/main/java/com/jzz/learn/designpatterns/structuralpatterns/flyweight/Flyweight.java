package com.jzz.learn.designpatterns.structuralpatterns.flyweight;
/**
 * 抽象享元角色
 * @author jzz
 * @date 2019-5-14
 */
public interface Flyweight {
    void operation(UnsharedConcreteFlyweight state);
}
