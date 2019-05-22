package com.jzz.learn.designpatterns.j2ee.interceptingfilter;

/**
 * 过滤器接口
 * @author jzz
 * @date 2019/5/22
 */
public interface Filter {
    void execute(String request);
}
