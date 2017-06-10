package com.sostvcn.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;


/**
 * 保存用户收藏的对象
 * Created by yubin on 2017/5/6.
 */

@Table(name = "sos_collect")
public class SosCollectEntity {


    @Id
    private int id;

    @Column
    private String objId;

    @Column
    private String objName;

    @Column
    private String objImage;

    @Column
    private String type;

    @Column
    private String objDesc;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

    @Column
    private String deleteFlag;


    public int getId() {
        return id;
    }

    public String getObjId() {
        return objId;
    }

    public String getObjName() {
        return objName;
    }

    public String getType() {
        return type;
    }

    public String getObjDesc() {
        return objDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setObjId(int objId) {
        this.objId = objId+"";
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setObjDesc(String objDesc) {
        this.objDesc = objDesc;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getObjImage() {
        return objImage;
    }

    public void setObjImage(String objImage) {
        this.objImage = objImage;
    }
}
