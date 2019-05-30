package com.jzz.newspeciality.java8.stream;

import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jzz
 * @date 2019/5/24
 */
public class Test {
    private static Stream<Integer> stream = Stream.of(1,2,3,4);

    public static void main(String[] args) {
        List<Long> list = new ArrayList<>();
        List<Long> collect = list.stream().collect(Collectors.toUnmodifiableList());
        collect.add(1L);
    }
}
