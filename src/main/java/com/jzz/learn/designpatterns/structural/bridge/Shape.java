package com.jzz.learn.designpatterns.structural.bridge;

/**
 * 抽象化角色
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
