package com.yjz.support.imageselector.iface;

import android.app.Activity;

import java.io.File;

public interface ICamera {
    /**
     * 拍照
     * @param ac
     * @param savePath 拍照图片保存路径
     * @param fileProvider sdk>=24时，提供provider
     * @param requestCode 拍照请求码
     */
    void capture(Activity ac, String savePath, String fileProvider, int requestCode);

    /**
     * 获取最后一次拍照图片路径
     * @return
     */
    String getLastCapturePath();

    /**
     * 裁剪图片
     * @param ac
     * @param srcFile 源文件对象
     * @param savePath 裁剪后图片保存路径
     * @param width 裁剪宽
     * @param height 裁剪高
     * @param fileProvider sdk>=24时，提供provider
     * @param requestCode 裁剪请求码
     */
    void crop(Activity ac, File srcFile, String savePath, int width, int height, String fileProvider, int requestCode);

    /**
     * 获取最后一次裁剪图片路径
     * @return
     */
    String getLastCropPath();




}
