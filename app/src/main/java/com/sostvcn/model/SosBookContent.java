package com.sostvcn.model;

/**
 * Created by Administrator on 2017\9\4 0004.
 */

public class SosBookContent {

    private int content_id;

    private String title;

    private int cate_id;

    private String content_text;

    private int ordering;

    private int prev;

    private int next;

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public int getContent_id() {
        return this.content_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getCate_id() {
        return this.cate_id;
    }

    public void setContent_text(String content_text) {
        this.content_text = content_text;
    }

    public String getContent_text() {
        return this.content_text;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public int getOrdering() {
        return this.ordering;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getPrev() {
        return this.prev;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getNext() {
        return this.next;
    }

}
