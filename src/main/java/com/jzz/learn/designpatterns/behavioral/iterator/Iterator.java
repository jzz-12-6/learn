package com.jzz.learn.designpatterns.behavioral.iterator;

/**
 * 抽象迭代器
 * @author jzz
 * @date 2019-5-16
 */
public interface Iterator {
    boolean hasNext();

    Object next();
}
