package com.sostvcn.service;

import android.text.TextUtils;
import android.util.Log;

import com.sostvcn.model.SosDownloadBean;
import com.sostvcn.utils.SDCardUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/6/10.
 */
public class DownloadThread implements Runnable {

    private SosDownloadBean bean;
    private File downloadFile;
    private String fileSize;
    private DownloadThreadListener listener;
    private static final String PATH = SDCardUtils.getSDCardRootDir();

    public DownloadThread(SosDownloadBean bean, DownloadThreadListener listener) {
        this.bean = bean;
        this.listener = listener;
    }

    @Override
    public void run() {
        downloadFile = new File(PATH + "/" + bean.getType() + "/" + bean.getTitle() + ".octmp");
        if (downloadFile.exists()) {
            fileSize = "bytes=" + downloadFile.length() + "-";
        }else{
            boolean ic = createFile(downloadFile.getPath());
        }

        //创建 OkHttp 对象相关
        OkHttpClient client = new OkHttpClient();

        //如果有临时文件,则在下载的头中添加下载区域
        Request request;
        if (!TextUtils.isEmpty(fileSize)) {
            request = new Request.Builder().url(bean.getUrl()).header("Range", fileSize).build();
        } else {
            request = new Request.Builder().url(bean.getUrl()).build();
        }

        Call call = client.newCall(request);
        try {
            bytes2File(call);
            listener.downloadCompleted(bean, Thread.currentThread());
        } catch (IOException e) {
            Log.e("OCException", "" + e);
            if (e.getMessage().contains("interrupted")) {
                Log.e("OCException", "Download task: " + bean.getUrl() + " Canceled");
                listener.downloadPaused(bean, Thread.currentThread());
            } else {
                listener.downloadFailed(bean, Thread.currentThread());
            }
        }

    }

    public interface DownloadThreadListener {
        void downloadPaused(SosDownloadBean bean, Runnable runnable);

        void downloadFailed(SosDownloadBean bean, Runnable runnable);

        void downloadCompleted(SosDownloadBean bean, Runnable runnable);

        void updateItem(SosDownloadBean bean, long totSize, long downSize);
    }


    // 创建单个文件
    public boolean createFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {// 判断文件是否存在
            System.out.println("目标文件已存在" + filePath);
            return false;
        }
        if (filePath.endsWith(File.separator)) {// 判断文件是否为目录
            System.out.println("目标文件不能为目录！");
            return false;
        }
        if (!file.getParentFile().exists()) {// 判断目标文件所在的目录是否存在
            // 如果目标文件所在的文件夹不存在，则创建父文件夹
            System.out.println("目标文件所在目录不存在，准备创建它！");
            if (!file.getParentFile().mkdirs()) {// 判断创建目录是否成功
                System.out.println("创建目标文件所在的目录失败！");
                return false;
            }
        }
        try {
            if (file.createNewFile()) {// 创建目标文件
                System.out.println("创建文件成功:" + filePath);
                return true;
            } else {
                System.out.println("创建文件失败！");
                return false;
            }
        } catch (IOException e) {// 捕获异常
            e.printStackTrace();
            System.out.println("创建文件失败！" + e.getMessage());
            return false;
        }
    }


    private void bytes2File(Call call) throws IOException {

        //设置输出流.
        OutputStream outPutStream;
        //检测是否支持断点续传
        Response response = call.execute();
        ResponseBody responseBody = response.body();
        String responeRange = response.headers().get("Content-Range");
        if (responeRange == null || !responeRange.contains(Long.toString(downloadFile.length()))) {
            outPutStream = new FileOutputStream(downloadFile, false);
        } else {
            outPutStream = new FileOutputStream(downloadFile, true);
        }
        InputStream inputStream = responseBody.byteStream();
        //如果有下载过的历史文件,则把下载总大小设为 总数据大小+文件大小 . 否则就是总数据大小
        if (TextUtils.isEmpty(fileSize)) {
            bean.setSize(responseBody.contentLength());
        } else {
            bean.setSize(responseBody.contentLength() + downloadFile.length());
        }

        int length;
        //设置缓存大小
        byte[] buffer = new byte[1024];
        //开始写入文件
        while ((length = inputStream.read(buffer)) != -1) {
            outPutStream.write(buffer, 0, length);
            onDownload(downloadFile.length());
        }
        //清空缓冲区
        outPutStream.flush();
        outPutStream.close();
        inputStream.close();
    }

    /**
     * 当产生下载进度时
     *
     * @param downloadedSize 当前下载的数据大小
     */
    public void onDownload(long downloadedSize) {
        bean.setDownloadedSize(downloadedSize);
        Log.d("下载进度", "名字:" + bean.getTitle() + "  总长:" + bean.getSize() + "  已下载:" + bean.getDownloadedSize());
        listener.updateItem(bean, bean.getSize(), downloadedSize);
    }
}
