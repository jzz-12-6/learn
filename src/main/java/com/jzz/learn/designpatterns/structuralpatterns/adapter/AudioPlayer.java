package com.jzz.learn.designpatterns.structuralpatterns.adapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jzz
 * @date 2019年5月13日
 */
@Slf4j
public class AudioPlayer implements MediaPlayer {
    private static final String VLC = "vlc";
    private static final String MP4 = "mp4";
    private static final String MP3 = "mp3";
    private MediaAdapter mediaAdapter;

    @Override
    public void play(String audioType, String fileName) {

        //播放 mp3 音乐文件的内置支持
        if(audioType.equalsIgnoreCase(MP3)){
            log.info("Playing mp3 file. Name: {}",fileName);
        }
        //mediaAdapter 提供了播放其他文件格式的支持
        else if(audioType.equalsIgnoreCase(VLC)
                || audioType.equalsIgnoreCase(MP4)){
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
        }
        else{
            log.info("Invalid media. {} format not supported",audioType );
        }
    }
}
