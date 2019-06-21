package com.jzz.newspeciality.java8.time.chrono;

import java.time.chrono.Chronology;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日历系统的抽象实现，用于组织和识别日期。
 * 实现Chronology 日历系统
 * @author jzz
 * @date 2019/6/21
 */
public class AbstractChronologyAPI {
    //私有属性
    /**
     * 可用日历系统的ID集合
     */
    private static final ConcurrentHashMap<String, Chronology> CHRONOS_BY_ID =new ConcurrentHashMap<>();
    /**
     * 可用日历系统的类型集合
     */
    private static final ConcurrentHashMap<String, Chronology> CHRONOS_BY_TYPE = new ConcurrentHashMap<>();

    /**
     * 注册日历
     * @param chrono
     * @return
     * Chronology registerChrono(Chronology chrono)
     */
//    static Chronology registerChrono(Chronology chrono) {
//        return registerChrono(chrono, chrono.getId());
//    }

    public static void main(String[] args) {
        Map<Object,Object> map = new HashMap<>();
        System.out.println(map.put(1,1));
        System.out.println(map.put(1,1));
        System.out.println(map.putIfAbsent(1,2));
        System.out.println(map.putIfAbsent(2,2));
    }
}
