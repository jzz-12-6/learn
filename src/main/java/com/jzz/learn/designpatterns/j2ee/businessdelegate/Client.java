package com.jzz.learn.designpatterns.j2ee.businessdelegate;

/**
 * 客户端
 * @author jzz
 * @date 2019/5/22
 */
public class Client {
    private BusinessDelegate businessService;

    public Client(BusinessDelegate businessService){
        this.businessService  = businessService;
    }

    public void doTask(){
        businessService.doTask();
    }
}
