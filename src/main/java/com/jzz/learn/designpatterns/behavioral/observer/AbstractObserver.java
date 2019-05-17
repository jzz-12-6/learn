package com.jzz.learn.designpatterns.behavioral.observer;

/**
 * 抽象目标
 * @author jzz
 * @date 2019/5/17
 */
public abstract class AbstractObserver {
    protected AbstractSubject abstractSubject;
    public abstract void update();
}
