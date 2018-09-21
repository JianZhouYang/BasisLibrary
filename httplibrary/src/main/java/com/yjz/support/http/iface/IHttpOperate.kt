package com.yjz.support.http.iface

import com.yjz.support.http.HttpRequest
import com.yjz.support.http.callback.DownloadCallback
import com.yjz.support.http.callback.ResponseCallback
import com.yjz.support.http.callback.UploadCallback


interface IHttpOperate{

    /**
     * get请求（同步）
     */
    fun <T> get(request: HttpRequest): T?

    /**
     * get请求(异步)
     */
    fun get(request: HttpRequest, callback: ResponseCallback?)

    /**
     * post请求(同步)
     */
    fun <T> post(request: HttpRequest): T?

    /**
     * post请求(异步)
     */
    fun post(request: HttpRequest, callback: ResponseCallback?)

    /**
     * 取消某人请求
     */
    fun cancel(tag: String)

    /**
     * 取消所有请求
     */
    fun cancelAll()

    /**
     * 下载文件
     */
    fun download(request: HttpRequest, callback: DownloadCallback?)

    /**
     * 上传文件
     */
    fun upload(request: HttpRequest, callback: UploadCallback?)
}