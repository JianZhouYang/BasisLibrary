package com.yjz.support.imageselector.callback;

import com.yjz.support.imageselector.base.FileItem;
import java.util.List;

public interface ImageCallback {

    enum Type{
        /**查询所有图片*/
        TYPE_ALL_IMAGES,
        /**查询某一目录下图片*/
        TYPE_DIR_IMAGES;
    }

    /**
     * 开始查询
     */
    void onQueryStart();

    /**
     * 查询完成
     * @param type 查询类型 TYPE_ALL_IMAGES/TYPE_DIR_IMAGES
     * @param list
     */
    void onQueryFinish(Type type, List<FileItem> list);
}
