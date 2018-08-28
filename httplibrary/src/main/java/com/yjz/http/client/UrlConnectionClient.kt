package com.yjz.http.client

import com.yjz.http.HttpRequest
import com.yjz.http.callback.DownloadCallback
import com.yjz.http.callback.ResponseCallback
import com.yjz.http.createGetUrl
import com.yjz.http.iface.IHttpClient
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

class UrlConnectionClient : IHttpClient<URLConnection?> {
    override fun getHttpClient(): URLConnection? = null

    override fun <T> get(request: HttpRequest): T? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> post(request: HttpRequest): T? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(request: HttpRequest, callback: ResponseCallback?) {
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
    }

    override fun post(request: HttpRequest, callback: ResponseCallback?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel(tag: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancelAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun download(request: HttpRequest, callback: DownloadCallback?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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