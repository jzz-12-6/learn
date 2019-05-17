package com.jzz.learn.designpatterns.behavioral.observer;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体观察者
 * @author jzz
 * @date 2019/5/17
 */
@Slf4j
public class BinaryObserver extends AbstractObserver {

    public BinaryObserver(AbstractSubject subject){
        this.abstractSubject = subject;
        this.abstractSubject.attach(this);
    }

    @Override
    public void update() {
        log.info("Binary String: {}",Integer.toBinaryString( abstractSubject.getState() ));
    }
}
