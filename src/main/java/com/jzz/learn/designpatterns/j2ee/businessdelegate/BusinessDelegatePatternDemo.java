package com.jzz.learn.designpatterns.j2ee.businessdelegate;

/**
 * @author jzz
 * @date 2019/5/22
 */
public class BusinessDelegatePatternDemo {
    public static void main(String[] args) {

        BusinessDelegate businessDelegate = new BusinessDelegate();
        businessDelegate.setServiceType("EJB");

        Client client = new Client(businessDelegate);
        client.doTask();

        businessDelegate.setServiceType("JMS");
        client.doTask();
    }
}
