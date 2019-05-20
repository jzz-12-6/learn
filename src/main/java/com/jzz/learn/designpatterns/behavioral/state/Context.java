package com.jzz.learn.designpatterns.behavioral.state;

/**
 * 环境类
 * @author jzz
 * @date 2019/5/20
 */
public class Context {
    private State state;

    /**
     * 定义环境类的初始状态
     */
    public Context(){
        state = null;
    }

    public void setState(State state){
        this.state = state;
    }

    public State getState(){
        return state;
    }
}
