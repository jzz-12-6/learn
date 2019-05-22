package com.jzz.learn.designpatterns.j2ee.interceptingfilter;

import lombok.extern.slf4j.Slf4j;

/**
 * 实体过滤器
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class AuthenticationFilter implements Filter {
    @Override
    public void execute(String request){
        log.info("Authenticating request: {}" , request);
    }
}
