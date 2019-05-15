package com.jzz.learn.designpatterns.structural.bridge;

import lombok.extern.slf4j.Slf4j;
/**
 * 具体实现化角色
 * @author jzz
 * @date 2019年5月13日
 */
@Slf4j
public class RedCircle implements DrawAPI {
    @Override
    public void drawCircle(int radius, int x, int y) {
        log.info("Drawing Circle[ color: red, radius: {}, x: {},y:{}]",radius,x,y);
    }
}
