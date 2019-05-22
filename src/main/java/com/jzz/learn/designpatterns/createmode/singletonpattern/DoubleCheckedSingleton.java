package com.jzz.learn.designpatterns.createmode.singletonpattern;

/**
 * 双检锁/双重校验锁
 * 这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
 * @author jzz
 * @date 2019/5/21
 */
public class DoubleCheckedSingleton {
    private static volatile DoubleCheckedSingleton singleton;
    private DoubleCheckedSingleton (){}
    public static DoubleCheckedSingleton getSingleton() {
        if (singleton == null) {
            synchronized (DoubleCheckedSingleton.class) {
                if (singleton == null) {
                    singleton = new DoubleCheckedSingleton();
                }
            }
        }
        return singleton;
    }
}
