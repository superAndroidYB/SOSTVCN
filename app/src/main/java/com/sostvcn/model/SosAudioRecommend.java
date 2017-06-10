package com.sostvcn.model;

/**
 * Created by Administrator on 2017/4/29.
 */
public class SosAudioRecommend {

    private int album_id;

    private String album_title;

    private int parent_id;

    private String playlist_id;

    private String description;

    private String cover;

    private Voice voice;

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
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setCover(String cover){
        this.cover = cover;
    }
    public String getCover(){
        return this.cover;
    }
    public void setVoice(Voice voice){
        this.voice = voice;
    }
    public Voice getVoice(){
        return this.voice;
    }

    public class Voice {
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
