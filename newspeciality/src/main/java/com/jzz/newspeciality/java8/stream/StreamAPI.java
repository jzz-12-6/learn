package com.jzz.newspeciality.java8.stream;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jzz
 * @date 2019/5/24
 */
public class StreamAPI {
    static List<Integer> list = new ArrayList<>();

    static {
       for(int i = 0 ;i<10;i++){
           list.add(i);
       }
    }

    /**
     * 窜行流
     * @return Stream
     */
    private static Stream<Integer> stream(){
        return list.stream();
    }
    /**
     * 并行流
     * @return Stream
     */
    private static Stream<Integer> parallelStream(){
        return list.parallelStream();
    }

    public static void main(String[] args) {
        //filter 用于通过设置的条件过滤出元素 不满足条件的被过滤
        List<Integer> filterList = stream().filter(i->i%2==0).collect(Collectors.toList());
        //map 用于映射每个元素到对应的结果 转换元素
        List<Long> mapList = stream().map(Integer::longValue).collect(Collectors.toList());
        //mapToInt 统计 类似mapToLong mapToDouble
        IntSummaryStatistics statistics = stream().mapToInt(Integer::intValue).summaryStatistics();
        //获取平均值
        statistics.getAverage();
        //获取个数
        statistics.getCount();
        //获取最大值
        statistics.getMax();
        //获取最小值
        statistics.getMin();
        //求和
        statistics.getSum();
        //设置和
        statistics.accept(1);
        //和另外一个统计相加
        statistics.combine(statistics);
        //去重
        stream().distinct();
        //排序
        stream().sorted();
        //指定排序器排序
        stream().sorted(Integer::compareTo);
        //接收一个Consumer的入参
        stream().peek(System.out::println);
        //用于获取指定数量的流
        stream().limit(10L);
        //扔掉指定数量的流
        stream().skip(10L);
        //使用一个断言作为参数，返回给定 Stream 的子集直到断言语句第一次返回 false
        //如果第一个值不满足断言条件，将返回一个空的 Stream。
        //a b c null d 返回 a b c
        stream().takeWhile(Objects::nonNull);
        //和 takeWhile 作用相反的，使用一个断言作为参数，直到断言语句第一次返回 true 才返回给定 Stream 的子集。
        //a b c null d 返回 d
        stream().dropWhile(Objects::nonNull);
        //类似 for loop循环
        stream().forEach(System.out::println);
        //有序的for loop循环
        stream().forEachOrdered(System.out::println);
        //转数组
        stream().toArray();
        stream().toArray(Integer[]::new);
        //把 Stream 元素组合起来。它提供一个起始值（种子），然后依照运算规则
        //求和
        /**
         * BinaryOperator<T> accumulator 累加器
         */
        stream().reduce(Integer::sum);
        /**
         * 两个参数，第一个参数是上次函数执行的返回值（也称为中间结果），
         * 第二个参数是stream中的元素
         */
        stream().reduce((x,y)->x+y);
        /**
         * @param identity T  指定初始值，若Stream为空，就直接返回该值。
         * @param accumulator BinaryOperator<T> 累加器 同上
         */
        stream().reduce(0,Integer::sum);
        /**
         * @param <U> 指定返回的类型
         * @param identity 指定初始值
         * @param accumulator 累加器  同上
         * @param combiner 第三个参数 parallelStream reduce操作是并发进行的 为了避免竞争 每个reduce线程都会有独立的result combiner的作用在于合并每个线程的result得到最终结果
         */
        stream().reduce(0,Integer::sum,Integer::sum);
        //max min 传入一个比较器，获取最大.最小值
        stream().max(Integer::compareTo);
        //获取流的个数
        stream().count();
        //是否有一个元素满足
        stream().anyMatch(Objects::isNull);
        //是否所有元素满足
        stream().allMatch(Objects::isNull);
        //是否所有元素都不满足
        stream().noneMatch(Objects::isNull);
        //返回第一个元素
        stream().findFirst();
        //返回任意一个元素
        stream().findAny();
    }

}
