package com.jzz.learn.designpatterns.j2ee.interceptingfilter;

/**
 * @author jzz
 * @date 2019/5/22
 */
public class Client {
    private FilterManager filterManager;

    public void setFilterManager(FilterManager filterManager){
        this.filterManager = filterManager;
    }

    public void sendRequest(String request){
        filterManager.filterRequest(request);
    }
}
