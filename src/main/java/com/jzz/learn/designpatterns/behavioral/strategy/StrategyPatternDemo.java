package com.jzz.learn.designpatterns.behavioral.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/20
 */
@Slf4j
public class StrategyPatternDemo {
    public static void main(String[] args) {
        Context context = new Context(new OperationAdd());
        log.info("10 + 5 = " + context.executeStrategy(10, 5));

        context = new Context(new OperationSubstract());
        log.info("10 - 5 = " + context.executeStrategy(10, 5));

        context = new Context(new OperationMultiply());
        log.info("10 * 5 = " + context.executeStrategy(10, 5));
    }
}
