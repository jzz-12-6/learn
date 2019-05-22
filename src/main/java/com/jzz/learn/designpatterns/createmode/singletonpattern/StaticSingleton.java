package com.jzz.learn.designpatterns.createmode.singletonpattern;

/**
 * 登记式/静态内部类
 * @author jzz
 * @date 2019/5/21
 */
public class StaticSingleton {
    private static class SingletonHolder {
        private static final StaticSingleton INSTANCE = new StaticSingleton();
    }
    private StaticSingleton (){}
    public  static StaticSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
