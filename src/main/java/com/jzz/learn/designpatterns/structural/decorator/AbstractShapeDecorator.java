package com.jzz.learn.designpatterns.structural.decorator;

/**
 * 抽象装饰角色
 * @author jzz
 * @date 2019-5-14
 */
public abstract class AbstractShapeDecorator implements Shape {
    protected Shape decoratedShape;

    public AbstractShapeDecorator(Shape decoratedShape){
        this.decoratedShape = decoratedShape;
    }
    @Override
    public void draw(){
        decoratedShape.draw();
    }
}
