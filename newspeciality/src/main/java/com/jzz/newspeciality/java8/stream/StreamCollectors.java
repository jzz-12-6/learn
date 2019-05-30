package com.jzz.newspeciality.java8.stream;

import java.util.*;
import java.util.stream.Collector;
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
        /**
         * 不可变 不能进行add remove操作
         */
//        Collectors.toUnmodifiableList();
//        Collectors.toUnmodifiableMap(()->,()->);
//        Collectors.toUnmodifiableSet();

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

        // groupingBy 分组统计
        /**
         *@param classifier Function 分组器 map的key
         */
        Map<Integer, List<String>> groupingBy1 = stringStream.collect(Collectors.groupingBy(String::length));
        /**
         *  @param downstream Collector 对与每个键相关联的值执行缩减操作
         * 统计数量
         */
        Map<Integer, Long> collect1 = stringStream.collect(Collectors.groupingBy(String::length, Collectors.counting()));
        /**
         * 按照长度分类，key为拼接字符串
         */
        Map<Integer, String> collect2 = stringStream.collect(Collectors.groupingBy(String::length, Collectors.mapping(l -> l, Collectors.joining(","))));
        /**
         * @param classifier Function 分组器 map的key
         * @param mapFactory  Supplier 容器
         * @param downstream Collector 对与每个键相关联的值执行缩减操作
         */
        stringStream.collect(Collectors.groupingBy(String::length,HashMap::new, Collectors.counting()));
        //partitioningBy

        /**
         * @param predicate Predicate 分组条件
         * true为一组，false为一组
         */
        Map<Boolean, List<String>> collect3 = stringStream.collect(Collectors.partitioningBy(String::isBlank));
        /**
         * @param downstream  Collector 分组的value
         */
        stringStream.collect(Collectors.partitioningBy(String::isBlank,Collectors.mapping(l->l,Collectors.joining(","))));

        //collectingAndThen
        /**
         * 包裹另一个转换器,对其结果应用转换函数
         * @param downstream 收集器
         * @param finisher 转换结果的收集器
         */
        List<String> collect4 = stringStream.map(l -> l).collect(Collectors.collectingAndThen(Collectors.toList(), l -> Collections.unmodifiableList(l)));
    }
}
