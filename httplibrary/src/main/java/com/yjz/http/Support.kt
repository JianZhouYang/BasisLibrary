package com.yjz.http

import com.yjz.http.callback.DownloadCallback
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URLEncoder

/**
 * 将传递进来的参数拼接成 url
 *
 * @param url
 * @param params
 * @return
 */
internal fun createGetUrl(url: String?, params: Map<String, String>): String {
    return if (null != url) {
        val sb = StringBuilder()
        sb.append(url)
        if (url.indexOf('&') > 0 || url.indexOf('?') > 0) {
            sb.append("&")
        } else {
            sb.append("?")
        }

        for ((key, value) in params) {
            val urlValue = URLEncoder.encode(value, "UTF-8")
            sb.append(key).append("=").append(urlValue).append("&")
        }
        sb.deleteCharAt(sb.length - 1)
        sb.toString()
    } else {
        ""
    }
}

internal fun downLoadSaveFile(inputStream: InputStream, totalSize: Long, request: HttpRequest, callback: DownloadCallback?): Boolean {
    var isSuccess = true
    var fos: FileOutputStream?
    var buf = ByteArray(100)
    var len = 0//本次读取的字节数
    var currSize: Long = 0//当前已经读取的字节数
    try {
        val file = request.getDownloadFileWrap()!!.file
        val dir = file.parentFile
        if (!dir.exists()) {
            dir.mkdirs()
        }
        fos = file.outputStream()
        inputStream.use {input ->
            fos.use {
                while (input.read(buf).also { len = it } != -1){
                    it.write(buf, 0, len)
                    currSize += len
                    callback?.onProgress(currSize, totalSize, len == -1)
                }
            }
        }
        fos!!.flush()
        callback?.onProgress(currSize, totalSize, len == -1)
    } catch (e: Exception) {
        e.printStackTrace()
        isSuccess = false
    }
    return isSuccess
}