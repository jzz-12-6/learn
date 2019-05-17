package com.jzz.learn.designpatterns.behavioral.observer;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体目标
 * @author jzz
 * @date 2019/5/17
 */
@Slf4j
public class ConcreteSubject extends AbstractSubject {

    @Override
    public void notifyAllObservers(){
        log.info("observer has changed");
        observers.forEach(AbstractObserver::update);
    }
}
