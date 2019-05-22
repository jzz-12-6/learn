package com.jzz.learn.designpatterns.j2ee.interceptingfilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤器链
 * @author jzz
 * @date 2019/5/22
 */
public class FilterChain {
    private List<Filter> filters = new ArrayList<Filter>();
    private Target target;

    public void addFilter(Filter filter){
        filters.add(filter);
    }

    public void execute(String request){
        for (Filter filter : filters) {
            filter.execute(request);
        }
        target.execute(request);
    }

    public void setTarget(Target target){
        this.target = target;
    }
}
