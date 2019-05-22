package com.jzz.learn.designpatterns.j2ee.dataaccessobject;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 数值对象
 * @author jzz
 * @date 2019/5/22
 */
@Data
@AllArgsConstructor
public class Student {
    private String name;
    private int rollNo;
}
