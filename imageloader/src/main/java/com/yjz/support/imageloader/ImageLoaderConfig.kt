package com.yjz.support.imageloader

import android.content.Context
import android.view.View
import com.squareup.okhttp.OkHttpClient

class ImageLoaderConfig private constructor(private val mBuilder: Builder){

    fun getContext(): Context = mBuilder.context;

    fun getImageLoader(): ILoader<*>? = mBuilder.imageLoader;

    fun getBaseDirectoryPath(): String? = mBuilder.baseDirectoryPath;

    fun getBaseDirectoryName(): String? = mBuilder.baseDirectoryName;

    fun getDownLoader(): IDownLoader? = mBuilder.downLoader;

    fun getOkHttpClient(): OkHttpClient? = mBuilder.okHttpClient;

    class Builder(context: Context){
        internal val context = context.applicationContext;
        internal var baseDirectoryName: String? = null;
        internal var baseDirectoryPath: String? = null;
        internal var downLoader: IDownLoader? = null;
        internal var imageLoader: ILoader<*>? = null;
        internal var okHttpClient: OkHttpClient? = null;

        fun <T : View> setLoader(loader: ILoader<T>): Builder{
            imageLoader = loader;
            return this;
        }


        /**
         * 设置图片缓存目录路径
         */
        fun setBaseDirectoryPath(directoryPath: String): Builder{
            baseDirectoryPath = directoryPath;
            return this;
        }

        /**
         * 设置图片缓存目录名称
         */
        fun setBaseDirectoryName(directoryName: String): Builder{
            baseDirectoryName = directoryName;
            return this;
        }

        fun setDownloader(downLoader: IDownLoader): Builder{
            this.downLoader = downLoader;
            return this;
        }

        fun setOkHttpClient(client: OkHttpClient): Builder{
            okHttpClient = client;
            return this;
        }


        fun create(): ImageLoaderConfig = ImageLoaderConfig(this);

    }

    interface IDownLoader{
        fun downLoad(path : String);
    }
}