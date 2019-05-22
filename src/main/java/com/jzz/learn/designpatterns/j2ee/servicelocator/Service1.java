package com.jzz.learn.designpatterns.j2ee.servicelocator;

import lombok.extern.slf4j.Slf4j;

/**
 * 实体服务
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class Service1 implements Service {
    @Override
    public void execute(){
        log.info("Executing Service1");
    }

    @Override
    public String getName() {
        return "Service1";
    }
}
