package com.jzz.newspeciality.java8.optional;

import java.util.Optional;

public class CreateOptional {
    public static void main(String[] args) {
        /**
         * 一个不为空的Optional
         * 为空抛出异常
         */
        Optional<Integer> integerOptional = Optional.of(1);
        /**
         * 一个可以为空的Optional
         * 为空时返回  Optional.empty();
         */
        Optional<Integer> integerOptional1 = Optional.ofNullable(2);
        /**
         * 一个空Optional
         */
        Optional<Object> empty = Optional.empty();
    }
}
