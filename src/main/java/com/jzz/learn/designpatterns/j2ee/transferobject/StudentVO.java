package com.jzz.learn.designpatterns.j2ee.transferobject;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 传输对象
 * @author jzz
 * @date 2019/5/22
 */
@Data
@AllArgsConstructor
public class StudentVO {
    private String name;
    private int rollNo;
}
