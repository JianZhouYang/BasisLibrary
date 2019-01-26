package com.yjz.support.http

import com.yjz.support.http.callback.DownloadCallback
import com.yjz.support.http.callback.ResponseCallback
import com.yjz.support.http.callback.UploadCallback
import com.yjz.support.http.iface.IHttpClient
import com.yjz.support.http.iface.IHttpOperate

/**
 * http工具类
 */
object HttpUtils : IHttpOperate {

    private lateinit var mClient: IHttpClient<*>


    fun <T>initialization(client: IHttpClient<T>) {
        if (this::mClient.isInitialized) {
            throw IllegalArgumentException("HttpClient已经初始化，无法重复进行初始化操作！")
        } else {
            mClient = client
        }
    }

    fun <T>getRealHttpClient(): T?{
        return mClient.getRealHttpClient()?.let {
            it as T
        }
    }

    override fun <T> get(request: HttpRequest): T? {
        return mClient.get(request)
    }

    override fun <T> post(request: HttpRequest): T? {
        return mClient.post(request)
    }

    override fun get(request: HttpRequest, callback: ResponseCallback?) = mClient.get(request, callback)

    override fun post(request: HttpRequest, callback: ResponseCallback?) = mClient.post(request, callback)

    override fun cancel(tag: String) = mClient.cancel(tag)

    override fun cancelAll() = mClient.cancelAll()

    override fun download(request: HttpRequest, callback: DownloadCallback?) = mClient.download(request, callback)

    override fun upload(request: HttpRequest, callback: UploadCallback?) = mClient.upload(request, callback)

}