package com.jzz.learn.designpatterns.behavioral.memento;

import lombok.Data;

/**
 * 发起人
 * @author jzz
 * @date 2019/5/16
 */
@Data
public class Originator {
    private String state;

    public Memento saveStateToMemento(){
        return new Memento(state);
    }

    public void getStateFromMemento(Memento Memento){
        state = Memento.getState();
    }
}
