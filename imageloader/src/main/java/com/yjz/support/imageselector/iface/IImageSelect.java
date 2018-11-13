package com.yjz.support.imageselector.iface;

import com.yjz.support.imageselector.callback.ImageCallback;

import java.util.List;

public interface IImageSelect {

    /**
     * 获取图片集合
     * @return
     */
    void getAllImages(ImageCallback callback);

    /**
     * 获取某一目录下的图片
     * @param dirName 目录名称
     * @return 返回该目录下的所有图片
     */
    void getImagesByDirName(String dirName, ImageCallback callback);

    /**
     * 获取图片目录名称集合
     * @return
     */
    List<String> getImageDirNames();
}
