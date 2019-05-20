package com.jzz.learn.designpatterns.behavioral.nullobject;

/**
 * @author jzz
 * @date 2019/5/20
 */
public class RealCustomer extends AbstractCustomer {

    public RealCustomer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isNil() {
        return false;
    }
}
