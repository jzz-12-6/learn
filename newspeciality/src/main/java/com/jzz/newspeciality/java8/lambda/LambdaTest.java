package com.jzz.newspeciality.java8.lambda;

/**
 * @author jzz
 * @date 2019/5/24
 */
public class LambdaTest {
    public static void main(String[] args) {
        new Thread(()-> System.out.println(1));
    }
}
