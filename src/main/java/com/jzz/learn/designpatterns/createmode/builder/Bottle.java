package com.jzz.learn.designpatterns.createmode.builder;

/**
 * @author jzz
 * @date 2019/5/21
 */
public class Bottle implements Packing {

    @Override
    public String pack() {
        return "Bottle";
    }
}
