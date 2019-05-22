package com.jzz.learn.designpatterns.j2ee.frontcontroller;

/**
 * @author jzz
 * @date 2019/5/22
 */
public class FrontControllerPatternDemo {
    public static void main(String[] args) {
        FrontController frontController = new FrontController();
        frontController.dispatchRequest("HOME");
        frontController.dispatchRequest("STUDENT");
    }
}
