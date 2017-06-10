package com.sostvcn.gateway.callback;


public  abstract class RxSubscriber<T> extends ErrorSubscriber<T> {

    /**
     *  开始请求网络
     */
    @Override
    public void onStart() {
        super.onStart();
        // todo some common as show loadding  and check netWork is NetworkAvailable
    }
    /**
     *   请求网络完成
     */
    @Override
    public void onCompleted() {
    }
    /**
     *  获取网络数据
     * @param t
     */
    @Override
    public void onNext(T t) {

          onSuccess(t);
    }
    public abstract  void onSuccess(T t);

}