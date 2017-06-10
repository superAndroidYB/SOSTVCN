package com.sostvcn.gateway.callback;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sostvcn.SostvApplication;
import com.sostvcn.gateway.handler.ProgressDialogHandler;
import com.sostvcn.utils.ToastUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/4/26.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener<T> mListener;
    private Context mContext;
    private ProgressDialogHandler mHandler;

    public ProgressSubscriber(SubscriberOnNextListener<T> listener, Context context) {
        this.mListener = listener;
        this.mContext = context;
        mHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog() {
        if (mHandler != null) {
            mHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mHandler != null) {
            mHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mHandler = null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            ToastUtils.show(SostvApplication.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT);
        } else if (e instanceof ConnectException) {
            ToastUtils.show(SostvApplication.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT);
        } else {
            Log.e("后台发生错误",e.getMessage());
            ToastUtils.show(SostvApplication.getContext(), "error:" + e.getMessage(), Toast.LENGTH_SHORT);
        }
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        if (mListener != null) {
            mListener.onNext(t);
        }
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
