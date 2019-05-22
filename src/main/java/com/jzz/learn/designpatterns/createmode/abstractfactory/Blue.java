package com.jzz.learn.designpatterns.createmode.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/21
 */
@Slf4j
public class Blue implements Color {

    @Override
    public void fill() {
        log.info("Inside Blue AbstractFactory ::fill() method.");
    }
}
