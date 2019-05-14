package com.jzz.learn.designpatterns.structuralpatterns.bridge;

import lombok.extern.slf4j.Slf4j;

/**
 * 桥接实现类
 * @author jzz
 * @date 2019年5月13日
 */
@Slf4j
public class GreenCircle implements DrawAPI {
    @Override
    public void drawCircle(int radius, int x, int y) {
        log.info("Drawing Circle[ color: green, radius: {}, x: {},y:{}]",radius,x,y);
    }
}
