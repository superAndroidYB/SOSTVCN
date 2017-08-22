package com.sostvcn.model;

import java.io.Serializable;

/**
 * Created by ldei on 2017/8/17.
 */

public class SosMagazines implements Serializable{

    private int cate_id;
    private String cate_title;
    private int parent_id;
    private String cover_image;

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }
    public int getCate_id() {
        return cate_id;
    }

    public void setCate_title(String cate_title) {
        this.cate_title = cate_title;
    }
    public String getCate_title() {
        return cate_title;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }
    public int getParent_id() {
        return parent_id;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }
    public String getCover_image() {
        return cover_image;
    }
}
