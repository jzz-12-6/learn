package com.jzz.learn.designpatterns.createmode.prototype;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/21
 */
@Slf4j
public class PrototypePatternDemo {
    public static void main(String[] args) {
        ShapeCache.loadCache();

        AbstractShape clonedShape =  ShapeCache.getShape("1");
        log.info("Shape : {}" , clonedShape.getType());

        AbstractShape clonedShape2 = ShapeCache.getShape("2");
        log.info("Shape : {}" , clonedShape2.getType());

        AbstractShape clonedShape3 = ShapeCache.getShape("3");
        log.info("Shape : {}" , clonedShape3.getType());
    }
}
