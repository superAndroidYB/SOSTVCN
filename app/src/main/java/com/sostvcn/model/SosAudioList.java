package com.sostvcn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/20.
 */
public class SosAudioList implements Serializable {

    private int album_id;

    private String album_title;

    private int parent_id;

    private String playlist_id;

    private String album_artist;

    private String album_source;

    private String album_cover;

    private String share_url;

    private List<Voice_list> voice_list ;

    public void setAlbum_id(int album_id){
        this.album_id = album_id;
    }
    public int getAlbum_id(){
        return this.album_id;
    }
    public void setAlbum_title(String album_title){
        this.album_title = album_title;
    }
    public String getAlbum_title(){
        return this.album_title;
    }
    public void setParent_id(int parent_id){
        this.parent_id = parent_id;
    }
    public int getParent_id(){
        return this.parent_id;
    }
    public void setPlaylist_id(String playlist_id){
        this.playlist_id = playlist_id;
    }
    public String getPlaylist_id(){
        return this.playlist_id;
    }
    public void setAlbum_artist(String album_artist){
        this.album_artist = album_artist;
    }
    public String getAlbum_artist(){
        return this.album_artist;
    }
    public void setAlbum_source(String album_source){
        this.album_source = album_source;
    }
    public String getAlbum_source(){
        return this.album_source;
    }
    public void setAlbum_cover(String album_cover){
        this.album_cover = album_cover;
    }
    public String getAlbum_cover(){
        return this.album_cover;
    }
    public void setShare_url(String share_url){
        this.share_url = share_url;
    }
    public String getShare_url(){
        return this.share_url;
    }
    public void setVoice_list(List<Voice_list> voice_list){
        this.voice_list = voice_list;
    }
    public List<Voice_list> getVoice_list(){
        return this.voice_list;
    }

    public static class Voice_list implements Serializable {
        private String title;

        private String mp3;

        private String download;

        private String cover;

        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setMp3(String mp3){
            this.mp3 = mp3;
        }
        public String getMp3(){
            return this.mp3;
        }
        public void setDownload(String download){
            this.download = download;
        }
        public String getDownload(){
            return this.download;
        }
        public void setCover(String cover){
            this.cover = cover;
        }
        public String getCover(){
            return this.cover;
        }
    }
}
