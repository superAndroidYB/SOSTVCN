package com.sostvcn.helper;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.sostvcn.model.SosDownloadBean;
import com.sostvcn.utils.SDCardUtils;

import java.util.Date;

/**
 * Created by yubin on 2017/5/6.
 * 视频下载的帮助类
 */
public class DownLoadHelper {

    private Context context;
    private DbUtils dbUtils;

    public DownLoadHelper(Context context) {
        this.context = context;
        this.dbUtils = DbUtils.create(context, SDCardUtils.getSDCardRootDir() + "/SOSTVDB", "SOSTVDB");
    }

    public void saveUpdateDownLoadBean(SosDownloadBean bean) throws DbException {
        bean.setUpdateTime(new Date());
        dbUtils.saveOrUpdate(bean);
    }

    public void deleteDownLoadBean(SosDownloadBean bean) throws DbException {
        dbUtils.delete(bean);
    }

}
