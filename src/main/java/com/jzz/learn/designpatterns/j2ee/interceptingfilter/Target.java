package com.jzz.learn.designpatterns.j2ee.interceptingfilter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class Target {
    public void execute(String request){
        log.info("Executing request: {}" , request);
    }
}
