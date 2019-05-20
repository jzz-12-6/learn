package com.jzz.learn.designpatterns.behavioral.nullobject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/20
 */
@Slf4j
public class NullPatternDemo {
    public static void main(String[] args) {

        AbstractCustomer customer1 = CustomerFactory.getCustomer("Rob");
        AbstractCustomer customer2 = CustomerFactory.getCustomer("Bob");
        AbstractCustomer customer3 = CustomerFactory.getCustomer("Julie");
        AbstractCustomer customer4 = CustomerFactory.getCustomer("Laura");

        log.info("Customers");
        log.info(customer1.getName());
        log.info(customer2.getName());
        log.info(customer3.getName());
        log.info(customer4.getName());
    }
}
