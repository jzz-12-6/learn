package com.jzz.learn.designpatterns.createmode.builder;

/**
 * 具体建造者
 * @author jzz
 * @date 2019/5/21
 */
public class Pepsi extends AbstractColdDrink {

    @Override
    public float price() {
        return 35.0f;
    }

    @Override
    public String name() {
        return "Pepsi";
    }
}
