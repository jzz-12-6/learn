package com.jzz.learn.designpatterns.structuralpatterns.bridge;

/**
 * DrawAPI 接口创建抽象类 Shape
 * @author jzz
 * @date 2019-5-13
 */
public abstract class Shape {
    protected DrawAPI drawAPI;
    protected Shape(DrawAPI drawAPI){
        this.drawAPI = drawAPI;
    }
    public abstract void draw();
}
