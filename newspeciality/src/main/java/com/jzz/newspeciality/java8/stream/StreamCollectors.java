package com.jzz.newspeciality.java8.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author jzz
 * @date 2019/5/28
 */
public class StreamCollectors {
    private static Stream<Integer> stream = Stream.of(1,2,3,4);
    private static Stream<String> stringStream = Stream.of("1","2","3");
    public static void main(String[] args) {
        //转list
        List<Integer> collectList = stream.collect(Collectors.toList());
        /**
         * @param supplier  Supplier 存储结果的容器
         * @param accumulator BiConsumer 累加器 元素添加到容器中调用的方法
         * @param combiner  BiConsumer 组合器  并行使用时的部分结果的组合器，并行流时生效
         */
        List<Integer> collectList2 = stream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        Set<Integer> collectSet = stream.collect(Collectors.toSet());
        Set<Integer> collectSet2 = stream.collect(HashSet::new,HashSet::add,HashSet::addAll);
        List<Integer> collectList3 = stream.collect(Collectors.toCollection(ArrayList::new));
        /**
         * 转map
         * @param keyMapper Function   流元素映射成键
         * @param valueMapper Function 流元素映射成值
         * 如果有重复的键抛出异常IllegalStateException
         */
        Map<Integer, Integer> collectMap = stream.collect(Collectors.toMap(i -> i, i -> i));
        Map<Long, Integer> collectMap2 = stream.collect(Collectors.toMap(Integer::longValue, i -> i+1));
        /**
         * @param mergeFunction BinaryOperator 合并函数，解决键相同时的保留策略
         *
         */
        Map<Integer, Integer> collectMap3 = stream.collect(Collectors.toMap(i -> i, i -> i,(oldValue,newValue)->{
            //保留旧值
            return oldValue;
            //其他操作
            //return oldValue+newValue;
        }));
        /**
         * @param mapFactory Supplier 提供新的空的容器，将结果插入其中
         */
        Map<Integer, Integer> collectMap4 = stream.collect(Collectors.toMap(i -> i, i -> i,(oldValue,newValue)->oldValue,HashMap::new));

        //join

        /**
         * 将所有字符拼接在一起
         * 返回1234
         */
        String collect = stringStream.collect(Collectors.joining());
        /**
         * @param delimiter 分隔符
         * 将所有字符拼接在一起
         *  返回1,2,3,4
         */
        stringStream.collect(Collectors.joining(","));
        /**
         * @param delimiter 分隔符
         * @param  prefix 前缀
         * @param  suffix  后缀
         */
        stringStream.collect(Collectors.joining(",","",""));

        Map<IntStream, List<String>> collect2 = stringStream.collect(Collectors.groupingBy(String::chars));


    }
}
