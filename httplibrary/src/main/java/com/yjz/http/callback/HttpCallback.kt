package com.yjz.http.callback


interface ResponseCallback {

    fun onSuccess(code: Int, content: String?, byteArray: ByteArray? = null)

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