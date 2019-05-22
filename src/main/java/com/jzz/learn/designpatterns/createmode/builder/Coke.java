package com.jzz.learn.designpatterns.createmode.builder;

/**
 * 具体建造者
 * @author jzz
 * @date 2019/5/21
 */
public class Coke extends AbstractColdDrink {

    @Override
    public float price() {
        return 30.0f;
    }

    @Override
    public String name() {
        return "Coke";
    }
}
