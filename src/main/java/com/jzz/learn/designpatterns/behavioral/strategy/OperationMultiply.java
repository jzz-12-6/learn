package com.jzz.learn.designpatterns.behavioral.strategy;

/**
 * 具体策略类
 * @author jzz
 * @date 2019/5/20
 */
public class OperationMultiply implements Strategy{
    @Override
    public int doOperation(int num1, int num2) {
        return num1 * num2;
    }
}
