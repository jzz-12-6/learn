package com.jzz.learn.designpatterns.behavioral.command;

/**
 * 具体命令
 * @author jzz
 * @date 2019-5-15
 */
public class BuyStock implements Order {
    private Stock abcStock;

    public BuyStock(Stock abcStock){
        this.abcStock = abcStock;
    }
    @Override
    public void execute() {
        abcStock.buy();
    }
}
