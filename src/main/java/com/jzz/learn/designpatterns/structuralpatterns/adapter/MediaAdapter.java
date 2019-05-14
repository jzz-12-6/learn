package com.jzz.learn.designpatterns.structuralpatterns.adapter;
/**
 * @author jzz
 * @date 2019年5月13日
 */
public class MediaAdapter implements MediaPlayer {

    private static final String VLC = "vlc";
    private static final String MP4 = "mp4";

    private AdvancedMediaPlayer advancedMusicPlayer;

    public MediaAdapter(String audioType){
        if(audioType.equalsIgnoreCase(VLC) ){
            advancedMusicPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase(MP4)){
            advancedMusicPlayer = new Mp4Player();
        }
    }

    @Override
    public void play(String audioType, String fileName) {
        if(audioType.equalsIgnoreCase(VLC)){
            advancedMusicPlayer.playVlc(fileName);
        }else if(audioType.equalsIgnoreCase(MP4)){
            advancedMusicPlayer.playMp4(fileName);
        }
    }
}
