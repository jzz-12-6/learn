package com.jzz.newspeciality.java8.function;

import org.junit.Test;

import java.util.function.Function;

/**
 * 接收一个对象T转换成对象R
 * @author jzz
 * @date 2019/7/9
 */
public class FunctionAPI {
    Function<Integer,Integer> function = integer -> integer*3;
    Function<Integer,Integer> function2 = integer -> integer*integer;
    @Test
    public void apply(){
        /**
         * 将对象T转为对象R
         * @param t 待转换的对象
         * @return r 转换后的对象
         * 3
         */
        Integer apply = function.apply(1);
    }
    @Test
    public void compose(){
        /**
         * 先执行参数，然后执行调用者
         * @param before 先执行对象
         * 48
         */
        Integer apply = function.compose(function2).apply(4);
    }

    @Test
    public void andThen(){
        /**
         * 先执行调用者，然后再执行参数
         * @param after 后执行参数
         * 144
         */
        Integer apply = function.andThen(function2).apply(4);
    }
}
