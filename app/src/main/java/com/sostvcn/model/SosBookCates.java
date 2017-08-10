package com.sostvcn.model;

import java.util.List;

/**
 * Created by Administrator on 2017/7/23.
 */
public class SosBookCates {

    private int cate_id;

    private String cate_title;

    private String cate_image;

    private String cate_type;

    private List<SosBookItems> subCates;

    private int subCatesTotalCount;

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getCate_id() {
        return this.cate_id;
    }

    public void setCate_title(String cate_title) {
        this.cate_title = cate_title;
    }

    public String getCate_title() {
        return this.cate_title;
    }

    public void setCate_image(String cate_image) {
        this.cate_image = cate_image;
    }

    public String getCate_image() {
        return this.cate_image;
    }

    public void setCate_type(String cate_type) {
        this.cate_type = cate_type;
    }

    public String getCate_type() {
        return this.cate_type;
    }

    public void setSubCates(List<SosBookItems> subCates) {
        this.subCates = subCates;
    }

    public List<SosBookItems> getSubCates() {
        return this.subCates;
    }

    public void setSubCatesTotalCount(int subCatesTotalCount) {
        this.subCatesTotalCount = subCatesTotalCount;
    }

    public int getSubCatesTotalCount() {
        return this.subCatesTotalCount;
    }
}
