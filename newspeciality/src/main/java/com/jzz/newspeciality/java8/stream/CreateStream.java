package com.jzz.newspeciality.java8.stream;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * 创建流
 * @author jzz
 * @date 2019/5/27
 */
public class CreateStream {
    public static void main(String[] args) {
        //一个参数的流
        Stream<Integer> stream = Stream.of(1);
        //一个不为空的流 参数为空则创建空流
        Stream<String> stringStream = Stream.ofNullable("1");
        //多个参数的流
        Stream<Integer> stream2 = Stream.of(1,2,3,4);
        //数组的流
        Stream<Integer> stream3 = Stream.of(new Integer[]{1,2,3});
        //builder建造流
        Stream<Integer> streamBuilder = Stream.<Integer>builder().add(1).add(2).build();
        //一个空流
        Stream<Object> empty = Stream.empty();
        /**
         * 创建一个流
         * @param seed the initial element 初始值
         * @param UnaryOperator 累加器
         * @param Predicate 过滤器
         */
        Stream<Integer> limit = Stream.iterate(1, l -> l + 1).limit(10);
        /**
         * 实现 Supplier 接口
         * 默认是串行但无序的
         * 是无限的必须用limit限制
         */
        Stream.generate(Math::random);
        /**
         * 合并两个流
         */
        Stream.concat(Stream.empty(),Stream.empty());
    }
}
