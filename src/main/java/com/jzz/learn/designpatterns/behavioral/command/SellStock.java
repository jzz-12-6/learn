package com.jzz.learn.designpatterns.behavioral.command;

/**
 * 具体命令
 * @author jzz
 * @date 2019-5-15
 */
public class SellStock implements Order {
    private Stock abcStock;

    public SellStock(Stock abcStock){
        this.abcStock = abcStock;
    }
    @Override
    public void execute() {
        abcStock.sell();
    }
}
