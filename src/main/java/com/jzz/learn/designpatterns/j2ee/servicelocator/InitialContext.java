package com.jzz.learn.designpatterns.j2ee.servicelocator;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始的 Context
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class InitialContext {
    public Object lookup(String jndiName){
        if(jndiName.equalsIgnoreCase("SERVICE1")){
            log.info("Looking up and creating a new Service1 object");
            return new Service1();
        }else if (jndiName.equalsIgnoreCase("SERVICE2")){
            log.info("Looking up and creating a new Service2 object");
            return new Service2();
        }
        return null;
    }
}
