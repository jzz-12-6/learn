package com.jzz.learn.designpatterns.structuralpatterns.adapter;

/**
 * 媒体播放器创建接口。
 * @author jzz
 * @date 2019年5月13日
 */
public interface MediaPlayer {
    void play(String audioType, String fileName);
}
