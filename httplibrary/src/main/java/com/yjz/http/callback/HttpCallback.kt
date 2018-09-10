package com.yjz.http.callback


interface ResponseCallback {

    /**
     * 请求响应成功
     */
    fun onSuccess(code: Int, content: String?, byteArray: ByteArray? = null)

    /**
     * 请求响应失败
     */
    fun onError(code: Int, message: String)
}

interface DownloadCallback : ResponseCallback{
    /**
     * @param bytesRead 当前读取响应体字节长度
     * @param contentLength 总字节长度
     * @param isDone 是否读取完成
     *
     */
    fun onProgress(bytesRead: Long, contentLength: Long, isDone: Boolean)
}

interface SimpleResponseCallback : ResponseCallback{
    /**
     * 请求开始之前
     */
    fun onStart()
}

