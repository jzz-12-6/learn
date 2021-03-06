package com.jzz.learn.designpatterns.behavioral.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理者
 * @author jzz
 * @date 2019/5/16
 */
public class CareTaker {
    private List<Memento> mementoList = new ArrayList<>();

    public void add(Memento state){
        mementoList.add(state);
    }

    public Memento get(int index){
        return mementoList.get(index);
    }
}
