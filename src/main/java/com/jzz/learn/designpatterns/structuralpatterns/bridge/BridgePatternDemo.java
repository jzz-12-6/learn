package com.jzz.learn.designpatterns.structuralpatterns.bridge;
/**
 * 使用 Shape 和 DrawAPI 类画出不同颜色的圆。
 * @author jzz
 * @date 2019-5-13
 */
public class BridgePatternDemo {
    public static void main(String[] args) {
        Shape redCircle = new Circle(100,100, 10, new RedCircle());
        Shape greenCircle = new Circle(100,100, 10, new GreenCircle());

        redCircle.draw();
        greenCircle.draw();
    }
}
