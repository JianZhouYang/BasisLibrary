package com.yjz.support.imageloader

import android.view.View
import java.io.File

interface ILoader<out T : View> {

    fun display(view: @UnsafeVariance T, url: String);

    fun display(view: @UnsafeVariance T, url: String, width: Int, height: Int);

    fun getUrl(path: Any, type: UrlType): String;

    fun clearCache(url: String);

    fun clearAllCache();

    fun getImageFileFromCache(url: String): File;

}

enum class UrlType{
    /**远程图片类型 */
    IMG_TYPE_NETWORK,
    /**本地图片类型 */
    IMG_TYPE_LOCAL,
    /**res资源图片类型 */
    IMG_TYPE_RES,
    /**asset目录下的资源 */
    IMG_TYPE_ASE
}