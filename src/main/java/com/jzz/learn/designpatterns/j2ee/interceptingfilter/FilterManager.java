package com.jzz.learn.designpatterns.j2ee.interceptingfilter;

/**
 * 过滤管理器
 * @author jzz
 * @date 2019/5/22
 */
public class FilterManager {
    private FilterChain filterChain;

    public FilterManager(Target target){
        filterChain = new FilterChain();
        filterChain.setTarget(target);
    }
    public void setFilter(Filter filter){
        filterChain.addFilter(filter);
    }

    public void filterRequest(String request){
        filterChain.execute(request);
    }
}
