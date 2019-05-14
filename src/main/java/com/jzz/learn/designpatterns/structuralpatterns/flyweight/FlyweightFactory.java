package com.jzz.learn.designpatterns.structuralpatterns.flyweight;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
/**
 * 享元工厂角色
 * @author jzz
 * @date 2019-5-14
 */
@Slf4j
public class FlyweightFactory {
    private HashMap<String, Flyweight> flyweights=new HashMap<>();
    public Flyweight getFlyweight(String key) {
        Flyweight flyweight=flyweights.get(key);
        if(flyweight!=null) {
            log.info("具体享元key:{}已经存在，被成功获取！",key);
        }
        else {
            flyweight=new ConcreteFlyweight(key);
            flyweights.put(key, flyweight);
        }
        return flyweight;
    }
}
