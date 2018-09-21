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

    private var mClient: IHttpClient<*>? = null

    fun <T>initialization(client: IHttpClient<T>) {
        if (null != mClient) {
            throw IllegalArgumentException("HttpClient已经初始化，无法重复进行初始化操作！")
        } else {
            mClient = client
        }
    }

    override fun <T> get(request: HttpRequest): T? {
        return if (null != mClient) {
            mClient?.get(request)
        } else {
            throwError()
            null
        }
    }

    override fun <T> post(request: HttpRequest): T? {
        return if (null != mClient) {
            mClient?.post(request)
        } else {
            throwError()
            null
        }
    }

    override fun get(request: HttpRequest, callback: ResponseCallback?) = mClient?.get(request, callback) ?: throwError()

    override fun post(request: HttpRequest, callback: ResponseCallback?) = mClient?.post(request, callback) ?: throwError()

    override fun cancel(tag: String) = mClient?.cancel(tag) ?: throwError()

    override fun cancelAll() = mClient?.cancelAll() ?: throwError()

    override fun download(request: HttpRequest, callback: DownloadCallback?) = mClient?.download(request, callback) ?: throwError()

    override fun upload(request: HttpRequest, callback: UploadCallback?) = mClient?.upload(request, callback) ?: throwError()

    private fun throwError(){
        throw IllegalArgumentException("HttpClient没有初始化，请先调用initialization()方法进行初始化操作！")
    }
}