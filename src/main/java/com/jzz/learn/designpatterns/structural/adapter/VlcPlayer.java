package com.jzz.learn.designpatterns.structural.adapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019年5月13日
 */
@Slf4j
public class VlcPlayer implements AdvancedMediaPlayer{
    @Override
    public void playVlc(String fileName) {
        log.info("Playing vlc file. Name: {}",fileName);
    }

    @Override
    public void playMp4(String fileName) {
        //什么也不做
    }
}
