package com.jzz.learn.designpatterns.structuralpatterns.decorator;

import lombok.extern.slf4j.Slf4j;

/**
 *具体构件角色
 * @author jzz
 * @date 2019-5-14
 */
@Slf4j
public class Circle implements Shape {

    @Override
    public void draw() {
        log.info("Shape: Circle");
    }
}
