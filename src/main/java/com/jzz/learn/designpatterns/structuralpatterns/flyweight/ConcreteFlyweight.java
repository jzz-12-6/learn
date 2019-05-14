package com.jzz.learn.designpatterns.structuralpatterns.flyweight;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体享元角色
 * @author jzz
 * @date 2019-5-14
 */
@Slf4j
public class ConcreteFlyweight implements Flyweight
{
    private String key;
    public ConcreteFlyweight(String key) {
        this.key=key;
        log.info("具体享元key:{}被创建！",key);
    }
    @Override
    public void operation(UnsharedConcreteFlyweight outState) {
        log.info("具体享元key:{}被调用！",key);
        log.info("非享元信息是:{}！",outState.getInfo());
    }
}
