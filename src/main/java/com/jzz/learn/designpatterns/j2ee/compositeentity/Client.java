package com.jzz.learn.designpatterns.j2ee.compositeentity;

import lombok.extern.slf4j.Slf4j;

/**
 * 策略 组合实体的客户端类
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class Client {
    private CompositeEntity compositeEntity = new CompositeEntity();

    public void printData(){
        for (int i = 0; i < compositeEntity.getData().length; i++) {
            log.info("Date: {}",compositeEntity.getData()[i]);
        }
    }

    public void setData(String data1, String data2){
        compositeEntity.setData(data1, data2);
    }
}
