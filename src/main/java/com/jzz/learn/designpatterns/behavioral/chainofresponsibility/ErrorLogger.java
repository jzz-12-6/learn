package com.jzz.learn.designpatterns.behavioral.chainofresponsibility;

import lombok.extern.slf4j.Slf4j;
/**
 * 具体处理者角色2
 * @author jzz
 * @date 2019-5-15
 */
@Slf4j
public class ErrorLogger extends AbstractLogger {

    public ErrorLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        log.info("Error Console::Logger: {}", message);
    }
}
