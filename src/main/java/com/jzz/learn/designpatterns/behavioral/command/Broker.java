package com.jzz.learn.designpatterns.behavioral.command;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用者
 * @author jzz
 * @date 2019-5-15
 */
public class Broker {
    private List<Order> orderList = new ArrayList<>();

    public void takeOrder(Order order){
        orderList.add(order);
    }

    public void placeOrders(){
        orderList.forEach(Order::execute);
        orderList.clear();
    }
}
