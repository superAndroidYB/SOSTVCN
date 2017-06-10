package com.sostvcn.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */
public class SosAudioCatesInfo {

    private int cate_id;

    private String cate_title;

    private String cate_image;

    private String cate_type;

    private List<SubCates> subCates ;

    public void setCate_id(int cate_id){
        this.cate_id = cate_id;
    }
    public int getCate_id(){
        return this.cate_id;
    }
    public void setCate_title(String cate_title){
        this.cate_title = cate_title;
    }
    public String getCate_title(){
        return this.cate_title;
    }
    public void setCate_image(String cate_image){
        this.cate_image = cate_image;
    }
    public String getCate_image(){
        return this.cate_image;
    }
    public void setCate_type(String cate_type){
        this.cate_type = cate_type;
    }
    public String getCate_type(){
        return this.cate_type;
    }
    public void setSubCates(List<SubCates> subCates){
        this.subCates = subCates;
    }
    public List<SubCates> getSubCates(){
        return this.subCates;
    }

    public class SubCates {
        private int cate_id;

        private int parent_id;

        private String cate_title;

        private String alias;

        private int playlist_id;

        private String cate_image;

        private String cate_type;

        private int voice_count;

        public void setCate_id(int cate_id){
            this.cate_id = cate_id;
        }
        public int getCate_id(){
            return this.cate_id;
        }
        public void setParent_id(int parent_id){
            this.parent_id = parent_id;
        }
        public int getParent_id(){
            return this.parent_id;
        }
        public void setCate_title(String cate_title){
            this.cate_title = cate_title;
        }
        public String getCate_title(){
            return this.cate_title;
        }
        public void setAlias(String alias){
            this.alias = alias;
        }
        public String getAlias(){
            return this.alias;
        }
        public void setPlaylist_id(int playlist_id){
            this.playlist_id = playlist_id;
        }
        public int getPlaylist_id(){
            return this.playlist_id;
        }
        public void setCate_image(String cate_image){
            this.cate_image = cate_image;
        }
        public String getCate_image(){
            return this.cate_image;
        }
        public void setCate_type(String cate_type){
            this.cate_type = cate_type;
        }
        public String getCate_type(){
            return this.cate_type;
        }
        public void setVoice_count(int voice_count){
            this.voice_count = voice_count;
        }
        public int getVoice_count(){
            return this.voice_count;
        }

    }
}
