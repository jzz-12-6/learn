package com.jzz.learn.designpatterns.createmode.builder;

/**
 * 抽象建造者
 * @author jzz
 * @date 2019/5/21
 */
public abstract class AbstractBurger implements Item {

    @Override
    public Packing packing() {
        return new Wrapper();
    }

    @Override
    public abstract float price();
}
