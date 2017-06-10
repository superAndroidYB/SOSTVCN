package com.sostvcn.model;

/**
 * Created by Administrator on 2017/5/1.
 */
public class RecentlyVideo {

    private int video_id;

    private int cate_id;

    private int video_duration;

    private String image;

    private String title;

    private String date;

    private String video_url_sd;

    private String video_url_hd;

    private String share_url;

    private String hits;

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

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
    public void setVideo_duration(int video_duration){
        this.video_duration = video_duration;
    }
    public int getVideo_duration(){
        return this.video_duration;
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
