package com.jzz.newspeciality.java8.function;

import org.junit.Test;

import java.util.function.Consumer;

/**
 * 接收一个对象进行处理但没有返回
 * @author jzz
 * @date 2019/7/6
 */
public class ConsumerAPI {

    @Test
    public void consumer(){
        /**
         * 用于接收一个对象进行处理但没有返回
         * 比如接收一个人并打印他的名字
         */
        Consumer<Integer> consumer = (i)-> {
            System.out.println(i);
        };
        /**
         * 对给定的参数执行此操作。
         */
        consumer.accept(1);
        /**
         * 按顺序执行操作，
         * 先执行accpet在执行after
         * 如果抛出异常不会执行andThen操作
         */
        consumer.andThen(integer -> System.out.println(integer+1)).accept(5);
    }

}
