package com.jzz.learn.designpatterns.behavioral.observer;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体观察者
 * @author jzz
 * @date 2019/5/17
 */
@Slf4j
public class OctalObserver extends AbstractObserver {

    public OctalObserver(AbstractSubject subject){
        this.abstractSubject = subject;
        this.abstractSubject.attach(this);
    }

    @Override
    public void update() {
        log.info("Hex String: {}",Integer.toOctalString( abstractSubject.getState() ));
    }
}
