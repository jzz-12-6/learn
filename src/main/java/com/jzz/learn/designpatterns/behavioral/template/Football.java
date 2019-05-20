package com.jzz.learn.designpatterns.behavioral.template;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体子类
 * @author jzz
 * @date 2019/5/20
 */
@Slf4j
public class Football extends AbstractGame {

    @Override
    void endPlay() {
        log.info("Football Game Finished!");
    }

    @Override
    void initialize() {
        log.info("Football Game Initialized! Start playing.");
    }

    @Override
    void startPlay() {
        log.info("Football Game Started. Enjoy the game!");
    }
}
