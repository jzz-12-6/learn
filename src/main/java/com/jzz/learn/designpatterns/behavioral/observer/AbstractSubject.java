package com.jzz.learn.designpatterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象目标
 * @author jzz
 * @date 2019/5/17
 */
public abstract class AbstractSubject {

    protected List<AbstractObserver> observers = new ArrayList<>();
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        notifyAllObservers();
    }

    /**
     * 增加观察者方法
     * @param abstractObserver  {@code AbstractObserver} 观察者
     */
    public void attach(AbstractObserver abstractObserver){
        observers.add(abstractObserver);
    }
    /**
     * 通知观察者方法
     */
    public abstract void notifyAllObservers();
}
