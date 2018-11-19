package com.yjz.support.imageselector.callback;

import com.yjz.support.imageselector.FileItem;
import java.util.List;

public interface ImageCallback {
    /**
     * 开始查询
     */
    void onQueryStart();

    /**
     * 查询完成
     * @param list
     */
    void onQueryFinish(List<FileItem> list);
}
