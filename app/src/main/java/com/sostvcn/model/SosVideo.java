package com.sostvcn.model;

/**
 * Created by Administrator on 2017/5/1.
 */
public class SosVideo {

    private String full_title;

    private int video_id;

    private String image;

    private String title;

    private String date;

    public void setFull_title(String full_title){
        this.full_title = full_title;
    }
    public String getFull_title(){
        return this.full_title;
    }
    public void setVideo_id(int video_id){
        this.video_id = video_id;
    }
    public int getVideo_id(){
        return this.video_id;
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
}
