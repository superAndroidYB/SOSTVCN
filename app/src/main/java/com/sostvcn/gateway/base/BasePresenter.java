package com.sostvcn.gateway.base;

import android.content.Context;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/4/23.
 */
public class BasePresenter<V extends  BaseView,M extends BaseModel> implements Presenter<V,M> {

    public Context mContext;

    protected V mView;

    protected M mModel;

    protected CompositeSubscription mCompositeSubscription;

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }
    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void attachView(V view) {
        this.mView=view;
    }

    @Override
    public void attachModel(M m) {
        this.mModel=m;
    }

    @Override
    public void detachView() {
        this.mView=null;
        unSubscribe();
    }

    public M getModel() {
        return mModel;
    }

    @Override
    public void detachModel() {
        this.mModel=null;
    }

    public V getView() {
        return mView;
    }

    public boolean isViewBind()
    {
        return mView!=null;
    }
}
