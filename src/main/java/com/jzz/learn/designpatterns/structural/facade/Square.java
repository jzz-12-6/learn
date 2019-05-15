package com.jzz.learn.designpatterns.structural.facade;

import lombok.extern.slf4j.Slf4j;

/**
 * 子系统（Sub System）角色：实现系统的部分功能，客户可以通过外观角色访问它。
 * @author jzz
 * @date 2019-5-14
 */
@Slf4j
public class Square implements Shape {

    @Override
    public void draw() {
        log.info("Square::draw()");
    }
}
