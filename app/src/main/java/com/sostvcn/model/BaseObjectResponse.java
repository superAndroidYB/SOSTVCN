package com.sostvcn.model;

/**
 * Created by Administrator on 2017/5/1.
 */
public class BaseObjectResponse<T> {

    private int code;

    private String message;

    private T results ;

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
    public void setResults(T results){
        this.results = results;
    }
    public T getResults(){
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

