package com.sostvcn.gateway.transformer;

import android.util.Log;

import com.sostvcn.gateway.exception.ExceptionHandle;
import com.sostvcn.gateway.exception.ServerException;
import com.sostvcn.model.BaseListResponse;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/4/23.
 */
public class ErrorTransformer<T> implements Observable.Transformer<BaseListResponse<T>,T> {
    @Override
    public Observable<T> call(Observable<BaseListResponse<T>> httpResponseObservable) {
//    对服务器端给出Json数据进行校验
        return httpResponseObservable.map(new Func1<BaseListResponse<T>, T>() {
            @Override
            public T call(BaseListResponse<T> tHttpResponse) {
                if (tHttpResponse.getCode()!=200) {
                    Log.e("cc",tHttpResponse.toString());
                    //如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
                    throw  new ServerException(String.valueOf(tHttpResponse.getResults()),tHttpResponse.getCode());
                }
//                //服务器请求数据成功，返回里面的数据实体
                return (T) tHttpResponse.getResults();
            }
//            对请求服务器出现错误信息进行拦截
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
            @Override
            public Observable<? extends T> call(Throwable throwable) {
                throwable.printStackTrace();
                return Observable.error(ExceptionHandle.handleException(throwable));
            }
        });
    }
    public static <T> ErrorTransformer<T> create() {
        return new ErrorTransformer<>();
    }

    private static ErrorTransformer instance = null;

    private ErrorTransformer(){
    }
    /**
     * 双重校验锁单例(线程安全)
     */
    public static <T>ErrorTransformer<T> getInstance() {
        if (instance == null) {
            synchronized (ErrorTransformer.class) {
                if (instance == null) {
                    instance = new ErrorTransformer();
                }
            }
        }
        return instance;
    }
}
