package com.sostvcn.model;

/**
 * Created by Administrator on 2017/7/23.
 */
public class SosBookItems {

    private int cate_id;

    private int parent_id;

    private String cate_title;

    private String alias;

    private String cate_image;

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getCate_id() {
        return this.cate_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getParent_id() {
        return this.parent_id;
    }

    public void setCate_title(String cate_title) {
        this.cate_title = cate_title;
    }

    public String getCate_title() {
        return this.cate_title;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setCate_image(String cate_image) {
        this.cate_image = cate_image;
    }

    public String getCate_image() {
        return this.cate_image;
    }

}
