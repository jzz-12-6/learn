package com.jzz.learn.designpatterns.structural.proxy.staticstate;

import lombok.extern.slf4j.Slf4j;

/**
 * 真实主题（Real AbstractSubject）类
 * 实现了抽象主题中的具体业务，是代理对象所代表的真实对象，是最终要引用的对象。
 * @author jzz
 * @date 2019-5-15
 */
@Slf4j
public class RealImage implements Image {

    private String fileName;

    public RealImage(String fileName){
        this.fileName = fileName;
        loadFromDisk(fileName);
    }

    @Override
    public void display() {
        log.info("Displaying :{}" , fileName);
    }

    private void loadFromDisk(String fileName){
        log.info("Loading :{}" , fileName);
    }
}
