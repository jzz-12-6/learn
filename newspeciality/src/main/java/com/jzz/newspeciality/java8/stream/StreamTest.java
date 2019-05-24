package com.jzz.newspeciality.java8.stream;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jzz
 * @date 2019/5/24
 */
public class StreamTest {
    static List<Integer> list = new ArrayList<>();

    static {
       for(int i = 0 ;i<1000;i++){
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
        //
        //stream().peek();
    }

}
