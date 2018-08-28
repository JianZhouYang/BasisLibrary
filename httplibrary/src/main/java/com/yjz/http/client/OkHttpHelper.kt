package com.yjz.http.client

import com.yjz.http.ErrorCode
import com.yjz.http.callback.ResponseCallback
import okhttp3.*
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

internal class OkHttpHelper(private val client: OkHttpClient) {

    fun get(request: Request, callback: ResponseCallback?, isReturnByteArray: Boolean) = processRequest(request, callback, isReturnByteArray)

    fun post(request: Request, callback: ResponseCallback?, isReturnByteArray: Boolean) = processRequest(request, callback, isReturnByteArray)


    private fun processRequest(request: Request, callback: ResponseCallback?, isReturnByteArray: Boolean){
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                processFailure(e, callback)
            }

            override fun onResponse(call: Call, response: Response) {
                if (null != response) {
                    if (response.isSuccessful) {
                        if (!isReturnByteArray) {
                            callback?.onSuccess(response.code(), response.body()?.string())
                        } else {
                            callback?.onSuccess(response.code(), null, response?.body()?.bytes())
                        }
                    } else {
                        callback?.onError(response.code(), response.message())
                    }
                } else {
                    callback?.onError(ErrorCode.CONNECT_ERROR.code, ErrorCode.CONNECT_ERROR.getErrorMsg())
                }
            }
        })
    }

    /**
     * 处理请求失败
     */
    private fun processFailure(e: IOException, callback: ResponseCallback?) {
        val error = when (e) {
            is TimeoutException -> ErrorCode.TIME_OUT
            is SocketTimeoutException -> ErrorCode.TIME_OUT
            else -> ErrorCode.CONNECT_ERROR
        }
        callback?.onError(error.code, error.getErrorMsg())
    }

}