package com.yjz.support.imageloader

import android.view.View
import java.io.File

object ImageLoader {
    @Volatile private var hasLoader: Boolean = false;
    private lateinit var mImageLoaderConfig: ImageLoaderConfig;

    fun initialize(imageLoaderConfig: ImageLoaderConfig){
        if (hasLoader) throw IllegalArgumentException("ImageLoader不能重复初始化！");
        hasLoader = true;
        mImageLoaderConfig = createConfig(imageLoaderConfig);
    }

    fun displayImage(view: View, imageUrl: String){
        check();
        mImageLoaderConfig.getImageLoader()!!.display(view, imageUrl);
    }

    fun displayImage(view: View, imageUrl: String, width: Int, height: Int){
        check();
        mImageLoaderConfig.getImageLoader()!!.display(view, imageUrl, width, height);
    }

    fun clearCache(url: String){
        check();
        mImageLoaderConfig.getImageLoader()!!.clearCache(url);
    }

    fun clearAllCache(){
        check();
        mImageLoaderConfig.getImageLoader()!!.clearAllCache();
    }

    fun getImageFileFromCache(url: String): File {
        check();
        return mImageLoaderConfig.getImageLoader()!!.getImageFileFromCache(url);
    }

    fun getUrlByType(imageUrl: Any, urlType: UrlType): String{
        check();
        return mImageLoaderConfig.getImageLoader()!!.getUrl(imageUrl, urlType);
    }

    private fun check(){
        if (!hasLoader) throw IllegalArgumentException("ImageLoader没有初始化！");
    }

    private fun createConfig(config: ImageLoaderConfig): ImageLoaderConfig{
        return ImageLoaderConfig.Builder(config.getContext())
                .setBaseDirectoryName(((if(null != config.getBaseDirectoryName()) config.getBaseDirectoryName() else "imageLoaderCache")!!))
                .setBaseDirectoryPath((if(null != config.getBaseDirectoryPath()) config.getBaseDirectoryPath() else config.getContext().getExternalFilesDir(null)!!.absolutePath)!!)
                .setDownloader((if (null != config.getDownLoader()) config.getDownLoader() else object : ImageLoaderConfig.IDownLoader{
                    override fun downLoad(path : String){
                        //Nothing
                    }
                })!!)
                .setLoader(FrescoImageLoader(config))
                .create();

    }
}