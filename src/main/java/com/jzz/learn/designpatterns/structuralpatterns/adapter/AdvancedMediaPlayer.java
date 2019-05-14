package com.jzz.learn.designpatterns.structuralpatterns.adapter;

/**
 * 高级的媒体播放器创建接口。
 * @author jzz
 * @date 2019年5月13日
 */
public interface AdvancedMediaPlayer {
     void playVlc(String fileName);
     void playMp4(String fileName);
}
