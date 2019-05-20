package com.jzz.learn.designpatterns.behavioral.visitor;

/**
 * @author jzz
 * @date 2019/5/20
 */
public class VisitorPatternDemo {
    public static void main(String[] args) {

        ComputerPart computer = new Computer();
        computer.accept(new ComputerPartDisplayVisitor());
    }
}
