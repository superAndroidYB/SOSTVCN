package com.sostvcn.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */
public class BaseListResponse<T> {

    private int code;

    private String message;

    private List<T> results ;

    private int count;

    private int nextPage;

    private int totalCount;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setResults(List<T> results){
        this.results = results;
    }
    public List<T> getResults(){
        return this.results;
    }
    public void setCount(int count){
        this.count = count;
    }
    public int getCount(){
        return this.count;
    }
    public void setNextPage(int nextPage){
        this.nextPage = nextPage;
    }
    public int getNextPage(){
        return this.nextPage;
    }
    public void setTotalCount(int totalCount){
        this.totalCount = totalCount;
    }
    public int getTotalCount(){
        return this.totalCount;
    }
}
