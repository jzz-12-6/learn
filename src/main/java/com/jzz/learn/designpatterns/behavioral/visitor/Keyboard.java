package com.jzz.learn.designpatterns.behavioral.visitor;

/**
 * 具体元素
 * @author jzz
 * @date 2019/5/20
 */
public class Keyboard  implements ComputerPart {

    @Override
    public void accept(ComputerPartVisitor computerPartVisitor) {
        computerPartVisitor.visit(this);
    }
}
