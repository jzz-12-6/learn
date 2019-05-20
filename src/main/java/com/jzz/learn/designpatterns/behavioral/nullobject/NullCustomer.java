package com.jzz.learn.designpatterns.behavioral.nullobject;

/**
 * @author jzz
 * @date 2019/5/20
 */
public class NullCustomer extends AbstractCustomer {

    @Override
    public String getName() {
        return "Not Available in Customer Database";
    }

    @Override
    public boolean isNil() {
        return true;
    }
}
