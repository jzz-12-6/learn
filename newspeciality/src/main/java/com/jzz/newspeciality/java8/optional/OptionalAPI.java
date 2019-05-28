package com.jzz.newspeciality.java8.optional;

import java.util.Objects;
import java.util.Optional;

/**
 * @author jzz
 * @date 2019/5/28
 */
public class OptionalAPI {
    private static   Optional<String[]> optional = Optional.ofNullable(new String[]{"1","2","3",null});


    public static void main(String[] args) {

        /**
         * 取得Optional中的值
         * 为null抛出异常
         */
        String[] strings = optional.get();
        /**
         * 是否非空
         */
        optional.isPresent();
        /**
         * 是否为空
         */
        optional.isEmpty();
        /**
         * @param action Consumer 无返回值的方法
         * 如果存在接收一个Consumer
         */
        optional.ifPresent(System.out::println);
        /**
         * @param action Consumer 无返回值的方法
         * @param emptyAction Runnable 不存在的时候执行的方法
         */
        optional.ifPresentOrElse(System.out::println,()-> System.out.println("empty"));
        /**
         * @param predicate Predicate
         * 过滤不满足条件的元素
         *  返回String[]{1,2,3}
         */
        Optional<String[]> filterOptional = optional.filter(Objects::nonNull);
        /**
         * @param mapper Function 转换
         */
        Optional<Integer> integer = optional.map(l -> {
            return l.length;
        });

        //optional.flatMap(l->l.length,);
        /**
         * @param supplier Supplier
         * 为空则返回新的Optional
         */
        Optional<String[]> or = optional.or(()->{
            Optional o = Optional.ofNullable(1);
            return o;
        });
        /**
         * @param other T 为空返回新对象
         */
        String[] strings1 = optional.orElse(new String[]{"4","5"});
        /**
         * 为空时返回对象
         * 和orElse区别，非空才会创建对象
         */
        optional.orElseGet(()->strings1);
        optional.orElseGet(()->{
            String[] s = new String[5];
            return s;
        });
        /**
         * 为空抛出异常
         */
        optional.orElseThrow();
        /**
         * 为空抛出指定异常
         */
        optional.orElseThrow(RuntimeException::new);
    }
}
