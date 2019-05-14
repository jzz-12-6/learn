package com.jzz.learn.designpatterns.structuralpatterns.adapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019年5月13日
 */
@Slf4j
public class Mp4Player implements AdvancedMediaPlayer{

    @Override
    public void playVlc(String fileName) {
        //什么也不做
    }

    @Override
    public void playMp4(String fileName) {
        log.info("Playing mp4 file. Name: {}",fileName);
    }
}
