package com.sostvcn.model;

/**
 * Created by Administrator on 2017/4/22.
 */
public class SosVideoRecommend {

    private int cate_id;

    private String cate_title;

    private SosVideo video;

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
    public void setVideo(SosVideo video){
        this.video = video;
    }
    public SosVideo getVideo(){
        return this.video;
    }

}
