package com.jzz.learn.designpatterns.behavioral.mediator;

/**
 * @author jzz
 * @date 2019/5/16
 */
public class MediatorDemo {
    public static void main(String[] args) {
        Mediator md=new ConcreteMediator();
        Colleague c1 = new ConcreteColleague1();
        Colleague c2 = new ConcreteColleague2();
        md.register(c1);
        md.register(c2);
        c1.send();
        c2.send();
    }
}
