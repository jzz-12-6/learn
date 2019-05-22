package com.jzz.learn.designpatterns.j2ee.compositeentity;

/**
 * @author jzz
 * @date 2019/5/22
 */
public class CompositeEntityPatternDemo {
    public static void main(String[] args) {
        Client client = new Client();
        client.setData("Test", "Data");
        client.printData();
        client.setData("Second Test", "Data1");
        client.printData();
    }
}
