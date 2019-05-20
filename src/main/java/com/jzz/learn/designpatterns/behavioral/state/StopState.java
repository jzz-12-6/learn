package com.jzz.learn.designpatterns.behavioral.state;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体状态类
 * @author jzz
 * @date 2019/5/20
 */
@Slf4j
public class StopState implements State {
    @Override
    public void doAction(Context context) {
        log.info("Player is in stop state");
        context.setState(this);
    }
    @Override
    public String toString(){
        return "Stop State";
    }
}
