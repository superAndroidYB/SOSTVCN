package com.sostvcn.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */
public class SosCatesInfo {

    private int cate_id;

    private String cate_title;

    private String cate_type;

    private List<SosSubCates> subCates ;

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
    public void setCate_type(String cate_type){
        this.cate_type = cate_type;
    }
    public String getCate_type(){
        return this.cate_type;
    }
    public void setSubCates(List<SosSubCates> subCates){
        this.subCates = subCates;
    }
    public List<SosSubCates> getSubCates(){
        return this.subCates;
    }
}
