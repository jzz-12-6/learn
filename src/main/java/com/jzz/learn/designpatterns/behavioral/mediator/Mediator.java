package com.jzz.learn.designpatterns.behavioral.mediator;

/**
 * 抽象中介者
 * @author jzz
 * @date 2019/5/16
 */
public abstract class Mediator {
    public abstract void register(Colleague colleague);

    /**
     * 转发
     * @param cl
     */
    public abstract void relay(Colleague cl);
}
