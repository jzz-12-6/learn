package com.jzz.learn.designpatterns.behavioral.mediator;

/**
 * 抽象同事类
 * @author jzz
 * @date 2019/5/16
 */
public abstract  class Colleague {
    protected Mediator mediator;
    public void setMedium(Mediator mediator)
    {
        this.mediator=mediator;
    }
    public abstract void receive();
    public abstract void send();
}
