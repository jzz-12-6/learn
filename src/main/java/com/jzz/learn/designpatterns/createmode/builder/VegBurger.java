package com.jzz.learn.designpatterns.createmode.builder;

/**
 * 具体建造者
 * @author jzz
 * @date 2019/5/21
 */
public class VegBurger extends AbstractBurger {

    @Override
    public float price() {
        return 25.0f;
    }

    @Override
    public String name() {
        return "Veg Burger";
    }
}
