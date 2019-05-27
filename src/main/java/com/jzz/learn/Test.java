package com.jzz.learn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jzz
 * @date 2019/5/23
 */
public class Test {
    private static List<Integer> list = new ArrayList<>(10000);
    static {
        for (int i = 0 ;i<10;i++){
            list.add(i);
        }
    }
    public static void main(String[] args) {
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()){
            Integer i = iterator.next();
            System.out.println(i);
            iterator.remove();
        }
    }
}
