package com.yjz.support.http.callback


interface ResponseCallback {

    /**
     * 请求响应成功
     * @param code 成功码
     * @param content 默认返回String类型; 当调用
     * HttpRequest.Builder().setReturnByteArray()设置true时，返回二进制数组
     */
    fun onSuccess(code: Int, content: Any?)

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

interface UploadCallback : ResponseCallback{
    fun onProgress(bytesWritten: Long, contentLength: Long, done: Boolean)
}

interface SimpleResponseCallback : ResponseCallback{
    /**
     * 请求开始之前
     */
    fun onStart()
}

