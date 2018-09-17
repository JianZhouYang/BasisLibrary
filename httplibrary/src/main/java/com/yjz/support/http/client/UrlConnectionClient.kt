package com.yjz.support.http.client

import com.yjz.support.http.*
import com.yjz.support.http.callback.DownloadCallback
import com.yjz.support.http.callback.ResponseCallback
import com.yjz.support.http.callback.SimpleResponseCallback
import com.yjz.support.http.iface.IHttpClient
import okhttp3.internal.Util
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder
import java.util.concurrent.*

class UrlConnectionClient : IHttpClient<URLConnection?> {
    private var mExecutorService: ExecutorService? = null

    @Synchronized
    private fun executorService(): ExecutorService{
        if (null == mExecutorService) {
            mExecutorService = ThreadPoolExecutor(0, Integer.MAX_VALUE, 60,
                    TimeUnit.SECONDS, SynchronousQueue<Runnable>(), Util.threadFactory("UrlConnection Dispatcher", false))
        }
        return mExecutorService!!
    }

    override fun getHttpClient(): URLConnection? = null

    override fun <T> get(request: HttpRequest): T? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> post(request: HttpRequest): T? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(request: HttpRequest, callback: ResponseCallback?) {
        if (null != callback && callback is SimpleResponseCallback) {
            callback.onStart()
        }
        executorService().execute{
            val con = createConnection(createGetUrl(request.getUrl(), request.getParams()))
            con.requestMethod = "GET"
            con.doOutput = false
            con.doInput = true
            con.connect()
            val responseCode  = con.responseCode
            if (HttpURLConnection.HTTP_OK == responseCode) {
                val reader = BufferedReader(InputStreamReader(con.inputStream))
                val sb = StringBuffer()
                var line: String? = ""
                reader.use {
                    while (it.readLine().also { line = it } != null){
                        sb.append(line)
                    }
                }
                callback?.onSuccess(responseCode, sb.toString())
            } else {
                callback?.onError(responseCode, con.responseMessage)
            }
            con.disconnect()
        }
    }

    override fun post(request: HttpRequest, callback: ResponseCallback?) {
        if (null != callback && callback is SimpleResponseCallback) {
            callback.onStart()
        }
        executorService().execute{
            val con = createConnection(request.getUrl())
            con.requestMethod = "POST"
            con.doOutput = true
            con.doInput = true
            val sb = StringBuilder()
            val map = request.getParams()
            for ((key, value) in map) {
                sb.append("&$key=").append(URLEncoder.encode(value))
            }
            if (sb.isNotEmpty()) {
                sb.deleteCharAt(0)
            }
            val out = PrintWriter(con.outputStream)
            out.print(sb.toString())
            out.flush()

            val responseCode  = con.responseCode
            if (HttpURLConnection.HTTP_OK == responseCode) {
                val reader = BufferedReader(InputStreamReader(con.inputStream))
                val sb = StringBuffer()
                var line: String? = ""
                reader.use {
                    while (it.readLine().also { line = it } != null){
                        sb.append(line)
                    }
                }
                callback?.onSuccess(responseCode, sb.toString())
            } else {
                callback?.onError(responseCode, con.responseMessage)
            }
            con.disconnect()
        }
    }

    override fun cancel(tag: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancelAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun download(request: HttpRequest, callback: DownloadCallback?) {
        request.getDownloadFileWrap()?.let {
            processDownload(request, callback)
        } ?: throw IllegalArgumentException("请使用DownloadFileBuilder对象创建request......")
    }

    private fun processDownload(request: HttpRequest, callback: DownloadCallback?){
        executorService().execute{
            val con = createConnection(createGetUrl(request.getUrl(), request.getParams()))
            if (request.getMethodType() == MethodType.GET) {
                con.requestMethod = "GET"
                con.doOutput = false
                con.doInput = true
            } else {
                con.requestMethod = "POST"
            }
            con.connect()
            val responseCode  = con.responseCode
            if (HttpURLConnection.HTTP_OK == responseCode) {
                if (null == request.getDownloadFileWrap()) {
                    callback?.onError(ErrorCode.DOWNLOAD_ERROR.code, ErrorCode.DOWNLOAD_ERROR.getErrorMsg())
                } else {
                    var size: Long = -1L
                    val list: MutableList<String>? = con.headerFields["Content-Length"]
                    list?.let {
                        size = Integer.valueOf(it[0]).toLong()
                    }
                    if (downLoadSaveFile(con.inputStream,
                                    size,
                                    request,
                                    callback)) {
                        callback?.onSuccess(responseCode, "download success......")
                    } else {
                        callback?.onError(ErrorCode.DOWNLOAD_ERROR.code, ErrorCode.DOWNLOAD_ERROR.getErrorMsg())
                    }
                }
            } else {
                callback?.onError(responseCode, con.responseMessage)
            }
            con.disconnect()
        }
    }

    private fun createConnection(url: String): HttpURLConnection{
        val realUrl = URL(url)
        val con = realUrl.openConnection() as HttpURLConnection
        // 设置通用的请求属性
        con.setRequestProperty("accept", "*/*")
        con.setRequestProperty("connection", "Keep-Alive")
        con.connectTimeout = 10 * 1000
        con.readTimeout = 10 * 1000
        return con
    }
}