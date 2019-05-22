package com.jzz.learn.designpatterns.createmode.singletonpattern;

/**
 * 懒汉式
 * 该模式的特点是类加载时没有生成单例，只有当第一次调用 getlnstance 方法时才去创建这个单例。
 * @author jzz
 * @date 2019/5/21
 */
public class LazySingleton {
    /**
     * 保证 instance 在所有线程中同步
     */
    private static volatile  LazySingleton instance;

    /**
     * private 避免类在外部被实例化
     */
    private LazySingleton (){}

    public static synchronized  LazySingleton getInstance() {
        //getInstance 方法前加同步
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
