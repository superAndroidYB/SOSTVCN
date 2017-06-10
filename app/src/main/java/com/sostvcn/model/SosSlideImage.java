package com.sostvcn.model;

/**
 * Created by Administrator on 2017/4/17.
 */
public class SosSlideImage {

    private int video_id;

    private String full_title;

    private int video_cate_id;

    private String title;

    private String date;

    private String cate_title;

    private String image;

    public void setVideo_id(int video_id){
        this.video_id = video_id;
    }
    public int getVideo_id(){
        return this.video_id;
    }
    public void setFull_title(String full_title){
        this.full_title = full_title;
    }
    public String getFull_title(){
        return this.full_title;
    }
    public void setVideo_cate_id(int video_cate_id){
        this.video_cate_id = video_cate_id;
    }
    public int getVideo_cate_id(){
        return this.video_cate_id;
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
    public void setCate_title(String cate_title){
        this.cate_title = cate_title;
    }
    public String getCate_title(){
        return this.cate_title;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return this.image;
    }
}
