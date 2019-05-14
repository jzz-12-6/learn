package com.jzz.learn.designpatterns.structuralpatterns.filter;

import java.util.List;
/**
 *
 * @author jzz
 * @date 2019-5-13
 */
public interface Criteria {
    List<Person> meetCriteria(List<Person> persons);
}
