package com.sostvcn.model;

/**
 * Created by Administrator on 2017/5/11.
 */
public class SosSubCates {

    private int cate_id;

    private int parent_id;

    private String cate_title;

    private String alias;

    private String cate_image_big;

    private String cate_type;

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
    public void setCate_image_big(String cate_image_big){
        this.cate_image_big = cate_image_big;
    }
    public String getCate_image_big(){
        return this.cate_image_big;
    }
    public void setCate_type(String cate_type){
        this.cate_type = cate_type;
    }
    public String getCate_type(){
        return this.cate_type;
    }
}
