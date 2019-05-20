package com.jzz.learn.designpatterns.behavioral.visitor;

/**
 * 抽象访问者
 * @author jzz
 * @date 2019/5/20
 */
public interface ComputerPartVisitor {
    void visit(Computer computer);

    void visit(Mouse mouse);

    void visit(Keyboard keyboard);

    void visit(Monitor monitor);
}
