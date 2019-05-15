package com.jzz.learn.designpatterns.behavioral.chainofresponsibility;

import lombok.extern.slf4j.Slf4j;
/**
 * 具体处理者角色1
 * @author jzz
 * @date 2019-5-15
 */
@Slf4j
public class ConsoleLogger extends AbstractLogger {

    public ConsoleLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        log.info("Standard Console::Logger: {}", message);
    }
}
