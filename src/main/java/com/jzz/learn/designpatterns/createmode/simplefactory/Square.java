package com.jzz.learn.designpatterns.createmode.simplefactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体产品
 * @author jzz
 * @date 2019/5/21
 */
@Slf4j
public class Square implements Shape {

    @Override
    public void draw() {
        log.info("Inside Square::draw() method.");
    }
}
