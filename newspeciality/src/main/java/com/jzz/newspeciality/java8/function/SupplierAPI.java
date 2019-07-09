package com.jzz.newspeciality.java8.function;

import org.junit.Test;

import java.util.function.Supplier;

/**
 * 提供一个对象
 * @author jzz
 * @date 2019/7/9
 */
public class SupplierAPI {

    Supplier<Integer> supplier = ()-> 1;
    @Test
    public void get(){
        /**
         * 获取对象
         */
        Integer integer = supplier.get();
    }
}
