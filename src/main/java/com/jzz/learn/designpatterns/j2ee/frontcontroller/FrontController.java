package com.jzz.learn.designpatterns.j2ee.frontcontroller;

import lombok.extern.slf4j.Slf4j;

/**
 * 前端控制器
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class FrontController {
    private Dispatcher dispatcher;

    public FrontController(){
        dispatcher = new Dispatcher();
    }

    private boolean isAuthenticUser(){
        log.info("User is authenticated successfully.");
        return true;
    }

    private void trackRequest(String request){
        log.info("Page requested: {}" , request);
    }

    public void dispatchRequest(String request){
        //记录每一个请求
        trackRequest(request);
        //对用户进行身份验证
        if(isAuthenticUser()){
            dispatcher.dispatch(request);
        }
    }
}
