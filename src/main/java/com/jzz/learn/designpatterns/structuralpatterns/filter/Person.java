package com.jzz.learn.designpatterns.structuralpatterns.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author jzz
 * @date 2019-5-13
 */
@Data
@AllArgsConstructor
public class Person {
    private String name;
    private String gender;
    private String maritalStatus;
}
