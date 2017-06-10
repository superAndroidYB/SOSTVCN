package com.sostvcn.model;

/**
 * Created by Administrator on 2017/4/29.
 */
public class SosBookRecommend {

    private int book_id;

    private String book_title;

    private int parent_id;

    private String image;

    public void setBook_id(int book_id){
        this.book_id = book_id;
    }
    public int getBook_id(){
        return this.book_id;
    }
    public void setBook_title(String book_title){
        this.book_title = book_title;
    }
    public String getBook_title(){
        return this.book_title;
    }
    public void setParent_id(int parent_id){
        this.parent_id = parent_id;
    }
    public int getParent_id(){
        return this.parent_id;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return this.image;
    }
}
