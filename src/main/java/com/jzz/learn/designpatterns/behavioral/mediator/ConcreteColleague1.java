package com.jzz.learn.designpatterns.behavioral.mediator;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体同事类
 * @author jzz
 * @date 2019/5/16
 */
@Slf4j
public class ConcreteColleague1 extends Colleague {
    @Override
    public void receive() {
        log.info("具体同事类1收到请求。");
    }
    @Override
    public void send() {
        log.info("具体同事类1发出请求。");
        //请中介者转发
        mediator.relay(this);
    }
}