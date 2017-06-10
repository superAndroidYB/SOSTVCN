package com.sostvcn.gateway.transformer;

import com.sostvcn.model.BaseListResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/23.
 */
public class DefaultTransformer<T>  implements Observable.Transformer<BaseListResponse<T>,T>{
    @Override
    public Observable<T> call(Observable<BaseListResponse<T>> httpResponseObservable) {

        return httpResponseObservable. subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .compose(ErrorTransformer.<T>getInstance())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
