package com.jzz.learn.designpatterns.j2ee.servicelocator;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存 Cache
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class Cache {

    private List<Service> services;

    public Cache(){
        services = new ArrayList<>();
    }

    public Service getService(String serviceName){
        for (Service service : services) {
            if(service.getName().equalsIgnoreCase(serviceName)){
                log.info("Returning cached  :{} object",serviceName);
                return service;
            }
        }
        return null;
    }

    public void addService(Service newService){
        boolean exists = false;
        for (Service service : services) {
            if(service.getName().equalsIgnoreCase(newService.getName())){
                exists = true;
            }
        }
        if(!exists){
            services.add(newService);
        }
    }
}
