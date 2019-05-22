package com.jzz.learn.designpatterns.j2ee.businessdelegate;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class EJBService implements BusinessService {

    @Override
    public void doProcessing() {
        log.info("Processing task by invoking EJB Service");
    }
}
