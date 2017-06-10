package com.sostvcn.model;

/**
 * Created by Administrator on 2017/5/1.
 */
public class SosCate {

    private int cate_id;

    private int parent_id;

    private String cate_title;

    private String cate_type;

    private String intro;

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
