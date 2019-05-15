package com.jzz.learn.designpatterns.structural.adapter;

/**
 * 目标（Target）接口
 * @author jzz
 * @date 2019年5月13日
 */
public interface MediaPlayer {
    void play(String audioType, String fileName);
}
