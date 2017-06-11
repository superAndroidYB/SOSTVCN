package com.sostvcn.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.sostvcn.helper.DownLoadHelper;
import com.sostvcn.model.SosDownloadBean;
import com.sostvcn.utils.Constants;
import com.sostvcn.utils.SDCardUtils;
import com.sostvcn.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/6/10.
 */
public class DownloadService extends Service {
    static final int MAX_DOWNLOADING_TASK = 2; //最大同时下载数
    static final int MAX_QUEUE_TASK = 50; //队列最大数量
    static final long KEEP_ALIVE_TIME = 60; //线程最大空闲时间
    private static final String PATH = SDCardUtils.getSDCardRootDir();

    private LocalBroadcastManager broadcastManager;
    private HashMap<String, SosDownloadBean> allTaskList;
    private ThreadPoolExecutor executor;
    private BlockingQueue queue;
    private DownLoadHelper dbHelper;


    @Override
    public void onCreate() {
        super.onCreate();

        //创建任务队列
        if (queue == null) {
            queue = new ArrayBlockingQueue(MAX_QUEUE_TASK, true);
        }

        //创建线程池
        if (executor == null) {
            executor = new ThreadPoolExecutor(MAX_DOWNLOADING_TASK, 10, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                    queue, new ThreadPoolExecutor.AbortPolicy());
        }

        if (dbHelper == null) {
            dbHelper = new DownLoadHelper(this);
        }
        if (allTaskList == null) {
            allTaskList = new HashMap<>();
        }

        //创建本地广播器
        if (broadcastManager == null) {
            broadcastManager = LocalBroadcastManager.getInstance(this);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SosDownloadBean bean = (SosDownloadBean) intent.getSerializableExtra("bean");
        checkTask(bean);
        return super.onStartCommand(intent, flags, startId);
    }

    private void checkTask(SosDownloadBean bean) {
        if (bean == null) {
            return;
        }

        if (new File(PATH + "/" + bean.getType() + "/" + bean.getTitle()).exists()) {
            ToastUtils.show(this, bean.getTitle() + "已经下载过啦！", Toast.LENGTH_SHORT);
        }

        //判断是否已经存在于任务列表中
        if (allTaskList.containsKey(bean.getUrl())) {
            bean = allTaskList.get(bean.getUrl());
            switch (bean.getStatus()) {
                case Constants.WAITTING:
                    ToastUtils.show(this, bean.getTitle() + "正在等待中！", Toast.LENGTH_SHORT);
                    break;
                case Constants.DOWNLOADING:
                    ToastUtils.show(this, bean.getTitle() + "正在下载中！", Toast.LENGTH_SHORT);
                    break;
                case Constants.PAUSED:

                    break;
                case Constants.FAILED:
                    startTask(bean);
                    break;
            }
        } else {
            startTask(bean);
        }

    }


    /**
     * 开始下载任务
     *
     * @param bean
     */
    private synchronized void startTask(SosDownloadBean bean) {
        bean.setStatus(Constants.DOWNLOADING);
        allTaskList.put(bean.getUrl(), bean);
        ToastUtils.show(this, bean.getTitle() + "开始下载中！", Toast.LENGTH_SHORT);
        executor.execute(new DownloadThread(bean, new DownloadListenter()));
    }

    private SosDownloadBean nextTask() {
        for (SosDownloadBean bean : allTaskList.values()) {
            if (Constants.WAITTING == bean.getStatus()) {
                bean.setStatus(Constants.PAUSED);
                return bean;
            }
        }
        return null;
    }

    class DownloadListenter implements DownloadThread.DownloadThreadListener {

        @Override
        public void downloadPaused(SosDownloadBean bean, Runnable runnable) {
            bean.setStatus(Constants.PAUSED);
            allTaskList.put(bean.getUrl(), bean);
            if (allTaskList.size() > 0) {
                //执行剩余的等待任务
                checkTask(nextTask());
            }
            if(runnable != null){
                executor.remove(runnable);
            }
            updateList();
        }

        @Override
        public void downloadFailed(SosDownloadBean bean, Runnable runnable) {
            bean.setStatus(Constants.FAILED);
            allTaskList.put(bean.getUrl(), bean);
            if (allTaskList.size() > 0) {
                //执行剩余的等待任务
                checkTask(nextTask());
            }
            if(runnable != null){
                executor.remove(runnable);
            }
            updateList();
        }

        @Override
        public void downloadCompleted(SosDownloadBean bean, Runnable runnable) {
            bean.setStatus(Constants.DOWNLOADED);
            allTaskList.put(bean.getUrl(), bean);
            if (allTaskList.size() > 0) {
                //执行剩余的等待任务
                checkTask(nextTask());
            }
            if(runnable != null){
                executor.remove(runnable);
            }
            updateList();

            try {
                dbHelper.saveUpdateDownLoadBean(bean);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateItem(SosDownloadBean bean, long totSize, long downSize) {
            int progressBarLength = (int) (((float)  downSize / totSize) * 100);
            Intent intent = new Intent(Constants.DL_UPDATE_ITEM);
            intent.putExtra("progressBarLength",progressBarLength);
            intent.putExtra("downloadedSize",String.format("%.2f", downSize/(1024.0*1024.0)));
            intent.putExtra("totalSize",String.format("%.2f", totSize/(1024.0*1024.0)));
            intent.putExtra("item",bean);
            broadcastManager.sendBroadcast(intent);
        }
    }

    /**
     * 更新整个下载列表
     */
    private void updateList(){
        broadcastManager.sendBroadcast(new Intent(Constants.DL_UPDATE_LIST));
    }

    public static void startService(Context context, SosDownloadBean bean) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        intent.setClass(context, DownloadService.class);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new GetServiceClass();
    }

    public class GetServiceClass extends Binder {

        public DownloadService getService() {
            return DownloadService.this;
        }
    }
}
