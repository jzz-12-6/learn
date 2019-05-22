package com.jzz.learn.designpatterns.createmode.prototype;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体原型类
 * @author jzz
 * @date 2019/5/21
 */
@Slf4j
public class Square extends AbstractShape {

    public Square(){
        type = "Square";
    }

    @Override
    public void draw() {
        log.info("Inside Square::draw() method.");
    }
}
