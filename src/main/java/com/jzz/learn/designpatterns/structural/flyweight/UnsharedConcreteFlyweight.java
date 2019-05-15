package com.jzz.learn.designpatterns.structural.flyweight;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * 非享元角色
 * @author jzz
 * @date 2019-5-14
 */
@Data
@AllArgsConstructor
public class UnsharedConcreteFlyweight {
    private String info;
}
