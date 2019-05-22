package com.jzz.learn.designpatterns.createmode.builder;

/**
 * 具体建造者
 * @author jzz
 * @date 2019/5/21
 */
public class ChickenBurger extends AbstractBurger {

    @Override
    public float price() {
        return 50.5f;
    }

    @Override
    public String name() {
        return "Chicken Burger";
    }
}
