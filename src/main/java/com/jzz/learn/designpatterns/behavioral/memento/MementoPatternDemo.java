package com.jzz.learn.designpatterns.behavioral.memento;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/16
 */
@Slf4j
public class MementoPatternDemo {
    public static void main(String[] args) {
        Originator originator = new Originator();
        CareTaker careTaker = new CareTaker();
        originator.setState("State #1");
        originator.setState("State #2");
        careTaker.add(originator.saveStateToMemento());
        originator.setState("State #3");
        careTaker.add(originator.saveStateToMemento());
        originator.setState("State #4");

        log.info("Current State: {}" , originator.getState());
        originator.getStateFromMemento(careTaker.get(0));
        log.info("First saved State: {}" , originator.getState());
        originator.getStateFromMemento(careTaker.get(1));
        log.info("Second saved State:{}" , originator.getState());
    }
}
