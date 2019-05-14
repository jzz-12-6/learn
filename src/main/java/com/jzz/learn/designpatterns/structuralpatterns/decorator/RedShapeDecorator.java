package com.jzz.learn.designpatterns.structuralpatterns.decorator;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * 具体装饰角色
 * @author jzz
 * @date 2019-5-14
 */
@Slf4j
public class RedShapeDecorator extends AbstractShapeDecorator {

    public RedShapeDecorator(Shape decoratedShape) {
        super(decoratedShape);
    }

    @Override
    public void draw() {
        decoratedShape.draw();
        setRedBorder(decoratedShape);
    }

    private void setRedBorder(Shape decoratedShape){
       log.info("Border Color: Red");
    }
}
