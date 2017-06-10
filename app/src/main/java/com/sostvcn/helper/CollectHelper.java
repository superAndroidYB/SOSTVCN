package com.sostvcn.helper;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.sostvcn.model.SosCollectEntity;
import com.sostvcn.utils.Constants;
import com.sostvcn.utils.SDCardUtils;

import java.util.Date;

/**
 * Created by yubin on 2017/5/6.
 * 视频收藏和取消收藏的帮助类
 */
public class CollectHelper {


    private Context context;
    private DbUtils dbUtils;


    public CollectHelper(Context context) {
        this.context = context;
        this.dbUtils = DbUtils.create(context, SDCardUtils.getSDCardRootDir() + "/SOSTVDB", "SOSTVDB");
    }

    public void saveCollect(SosCollectEntity entity) {
        entity.setCreateTime(new Date());
        entity.setDeleteFlag(Constants.NO);
        try {
            dbUtils.saveBindingId(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void deleteCollect(SosCollectEntity entity){
        //entity.setDeleteFlag(Constants.YES);
        try {
            //dbUtils.update(entity,"deleteFlag");
            dbUtils.delete(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public SosCollectEntity findCollect(int id){
        return findCollect(id+"");
    }

    public SosCollectEntity findCollect(String id){
        try {
            if(dbUtils.tableIsExist(SosCollectEntity.class)){
                SosCollectEntity entity = dbUtils.findFirst(Selector.from(SosCollectEntity.class)
                        .where("deleteFlag","=",Constants.NO).and("objId","=",id));
                return entity;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
