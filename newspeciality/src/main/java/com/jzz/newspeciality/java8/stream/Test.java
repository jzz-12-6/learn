package com.jzz.newspeciality.java8.stream;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author jzz
 * @date 2019/5/24
 */
public class Test {
    public static void main(String[] args) {
        Stream<Integer> stream = Stream.of(1,2,3,4);
        stream.peek();
    }
}
