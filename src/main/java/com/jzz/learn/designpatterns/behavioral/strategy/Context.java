package com.jzz.learn.designpatterns.behavioral.strategy;

/**
 * 环境类
 * @author jzz
 * @date 2019/5/20
 */
public class Context {
    private Strategy strategy;

    public Context(Strategy strategy){
        this.strategy = strategy;
    }

    public int executeStrategy(int num1, int num2){
        return strategy.doOperation(num1, num2);
    }
}
