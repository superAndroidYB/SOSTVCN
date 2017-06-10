package com.sostvcn.gateway.callback;


import android.util.Log;

import com.sostvcn.gateway.exception.ResponeThrowable;

import rx.Subscriber;


public abstract class ErrorSubscriber<T> extends Subscriber<T> {
    @Override
    public void onError(Throwable e) {
        Log.e("错误信息","错误信息:"+e.getMessage());
        if(e instanceof ResponeThrowable){
            onError((ResponeThrowable)e);
        }else{

            onError(new ResponeThrowable(e,1000));
        }
    }
    /**
     * 错误回调
     */
    protected abstract void onError(ResponeThrowable ex);
}

