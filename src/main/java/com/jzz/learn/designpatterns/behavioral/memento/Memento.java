package com.jzz.learn.designpatterns.behavioral.memento;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 备忘录
 * @author jzz
 * @date 2019/5/16
 */
@Data
@AllArgsConstructor
public class Memento {
    private String state;
}
