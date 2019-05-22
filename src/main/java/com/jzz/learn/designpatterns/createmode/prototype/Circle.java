package com.jzz.learn.designpatterns.createmode.prototype;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体原型类
 * @author jzz
 * @date 2019/5/21
 */
@Slf4j
public class Circle extends AbstractShape {

    public Circle(){
        type = "Circle";
    }

    @Override
    public void draw() {
        log.info("Inside Circle::draw() method.");
    }
}
