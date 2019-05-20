package com.jzz.learn.designpatterns.behavioral.state;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/20
 */
@Slf4j
public class StatePatternDemo {
    public static void main(String[] args) {
        Context context = new Context();

        StartState startState = new StartState();
        startState.doAction(context);

        log.info(context.getState().toString());

        StopState stopState = new StopState();
        stopState.doAction(context);

        log.info(context.getState().toString());
    }
}
