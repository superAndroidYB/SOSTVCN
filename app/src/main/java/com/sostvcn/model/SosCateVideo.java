package com.sostvcn.model;

/**
 * Created by Administrator on 2017/5/1.
 */
public class SosCateVideo {


    private Video video;

    private Cate cate;

    public void setVideo(Video video){
        this.video = video;
    }
    public Video getVideo(){
        return this.video;
    }
    public void setCate(Cate cate){
        this.cate = cate;
    }
    public Cate getCate(){
        return this.cate;
    }

    public class Cate {
        private int cate_id;

        private int parent_id;

        private String cate_title;

        private String cate_type;

        private String intro;

        private String updateTo;

        public String getUpdateTo() {
            return updateTo;
        }

        public void setUpdateTo(String updateTo) {
            this.updateTo = updateTo;
        }

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
        public void setCate_type(String cate_type){
            this.cate_type = cate_type;
        }
        public String getCate_type(){
            return this.cate_type;
        }
        public void setIntro(String intro){
            this.intro = intro;
        }
        public String getIntro(){
            return this.intro;
        }

    }

    public class Video {
        private int video_id;

        private int cate_id;

        private int hits;

        private String image;

        private String title;

        private String date;

        private String video_url_sd;

        private String video_url_hd;

        private String share_url;

        public void setVideo_id(int video_id){
            this.video_id = video_id;
        }
        public int getVideo_id(){
            return this.video_id;
        }
        public void setCate_id(int cate_id){
            this.cate_id = cate_id;
        }
        public int getCate_id(){
            return this.cate_id;
        }
        public void setHits(int hits){
            this.hits = hits;
        }
        public int getHits(){
            return this.hits;
        }
        public void setImage(String image){
            this.image = image;
        }
        public String getImage(){
            return this.image;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setDate(String date){
            this.date = date;
        }
        public String getDate(){
            return this.date;
        }
        public void setVideo_url_sd(String video_url_sd){
            this.video_url_sd = video_url_sd;
        }
        public String getVideo_url_sd(){
            return this.video_url_sd;
        }
        public void setVideo_url_hd(String video_url_hd){
            this.video_url_hd = video_url_hd;
        }
        public String getVideo_url_hd(){
            return this.video_url_hd;
        }
        public void setShare_url(String share_url){
            this.share_url = share_url;
        }
        public String getShare_url(){
            return this.share_url;
        }

    }
}
