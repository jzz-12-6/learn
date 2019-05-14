package com.jzz.learn.designpatterns.structuralpatterns.facade;

import lombok.Data;

/**
 * 客户（Client）角色：通过一个外观角色访问各个子系统的功能。
 * @author jzz
 * @date 2019-5-14
 */
@Data
public class ShapeMaker {
    private Shape circle;
    private Shape rectangle;
    private Shape square;

    public void drawCircle(){
        circle.draw();
    }
    public void drawRectangle(){
        rectangle.draw();
    }
    public void drawSquare(){
        square.draw();
    }
}
