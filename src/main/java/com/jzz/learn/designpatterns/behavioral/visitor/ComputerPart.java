package com.jzz.learn.designpatterns.behavioral.visitor;

/**
 * 抽象元素
 * @author jzz
 * @date 2019/5/20
 */
public interface ComputerPart {
    void accept(ComputerPartVisitor computerPartVisitor);
}
