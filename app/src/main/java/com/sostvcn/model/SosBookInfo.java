package com.sostvcn.model;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
public class SosBookInfo {

    private int book_id;
    private String book_title;
    private String book_cover;
    private List<BookContent> contents;
    private int hits;

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_cover(String book_cover) {
        this.book_cover = book_cover;
    }

    public String getBook_cover() {
        return book_cover;
    }

    public void setContents(List<BookContent> contents) {
        this.contents = contents;
    }

    public List<BookContent> getContents() {
        return contents;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getHits() {
        return hits;
    }

    class BookContent {
        private int content_id;
        private String title;
        private int cate_id;

        public void setContent_id(int content_id) {
            this.content_id = content_id;
        }

        public int getContent_id() {
            return content_id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
        }

        public int getCate_id() {
            return cate_id;
        }
    }
}
