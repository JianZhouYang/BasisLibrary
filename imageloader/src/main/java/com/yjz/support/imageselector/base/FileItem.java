package com.yjz.support.imageselector.base;

import java.io.Serializable;

public class FileItem implements Serializable {

    /**拍照*/
    public static final String ACTION_TYPE_CAMERA = "action.type.camera";
    /**显示*/
    public static final String ACTION_TYPE_SHOW = "action.type.show";

    /**图片路径*/
    public String path;

    /**图片名称*/
    public String name;

    /**图片大小*/
    public String size;

    /**图片添加日期*/
    public String date;

    /**图片目录名称*/
    public String parentDirName;

    public String actionType;


    public FileItem(String path, String name, String size, String date){
        this.path = path;
        this.name = name;
        this.size = size;
        this.date = date;
        this.actionType = ACTION_TYPE_SHOW;
    }

}
