package com.jzz.newspeciality.java8.function;

import org.junit.Test;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 提供断言操作，返回boolean值
 * @author jzz
 * @date 2019/7/9
 */
public class PredicateAPI {
    /**
     * 创建一个断言
      */
    Predicate<Integer> predicate = integer-> integer == 6 || integer == 5;
    @Test
    public void test(){
        /**
         * 是否满足断言条件
         * true
         */
        boolean test = predicate.test(5);
    }
    @Test
    public void and(){
        /**
         * &&操作
         * false
         */
        boolean test = predicate.and(Objects::nonNull).test(7);
    }
    @Test
    public void negate(){
        /**
         * 取反操作
         * false
         */
        boolean test = predicate.negate().test(6);
    }
    @Test
    public void or(){
        /**
         * || 操作
         * true
         */
        boolean test = predicate.or(Objects::nonNull).test(6);
    }
}
