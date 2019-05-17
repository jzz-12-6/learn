package com.jzz.learn.designpatterns.behavioral.observer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/17
 */
@Slf4j
public class ObserverPatternDemo {
    public static void main(String[] args) {
        AbstractSubject subject = new ConcreteSubject();

        new HexaObserver(subject);
        new OctalObserver(subject);
        new BinaryObserver(subject);
        log.info("First state change: 15");
        subject.setState(15);
        log.info("Second state change: 10");
        subject.setState(10);
    }
}
