package com.jzz.learn.designpatterns.behavioral.template;

import lombok.extern.slf4j.Slf4j;

/**
 * 体子类
 * @author jzz
 * @date 2019/5/20
 */
@Slf4j
public class Cricket extends AbstractGame {

    @Override
    void endPlay() {
        log.info("Cricket Game Finished!");
    }

    @Override
    void initialize() {
        log.info("Cricket Game Initialized! Start playing.");
    }

    @Override
    void startPlay() {
        log.info("Cricket Game Started. Enjoy the game!");
    }
}
