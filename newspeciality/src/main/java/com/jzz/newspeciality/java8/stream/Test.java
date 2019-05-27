package com.jzz.newspeciality.java8.stream;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        Stream<Integer> limit = Stream.iterate(1, l -> l + 1).limit(10);
        Stream.generate(Math::random);
        System.out.println(limit.count());

    }
}
