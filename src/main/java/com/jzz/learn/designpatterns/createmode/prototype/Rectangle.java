package com.jzz.learn.designpatterns.createmode.prototype;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体原型类
 * @author jzz
 * @date 2019/5/21
 */
@Slf4j
public class Rectangle extends AbstractShape {

    public Rectangle(){
        type = "Rectangle";
    }

    @Override
    public void draw() {
        log.info("Inside Rectangle::draw() method.");
    }
}
