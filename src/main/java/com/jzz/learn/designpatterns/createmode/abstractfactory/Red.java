package com.jzz.learn.designpatterns.createmode.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体产品
 * @author jzz
 * @date 2019/5/21
 */
@Slf4j
public class Red implements Color {

    @Override
    public void fill() {
        log.info("Inside Red::fill() method.");
    }
}
