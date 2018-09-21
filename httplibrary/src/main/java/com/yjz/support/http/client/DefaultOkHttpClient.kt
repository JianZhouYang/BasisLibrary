package com.yjz.support.http.client

import com.yjz.support.http.*
import com.yjz.support.http.callback.DownloadCallback
import com.yjz.support.http.callback.ResponseCallback
import com.yjz.support.http.callback.SimpleResponseCallback
import com.yjz.support.http.callback.UploadCallback
import com.yjz.support.support.http.client.OkHttpHelper
import okhttp3.*
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class DefaultOkHttpClient : BaseHttpClient<Request, OkHttpClient>() {

    override fun conversionRequest(request: HttpRequest): Request {
        val builder = Request.Builder()

        if (null != request.getTag()) {
            builder.tag(request.getTag())
        }

        if (request.getHeaders().isNotEmpty()) {
            request.getHeaders().forEach{
                builder.addHeader(it.key, it.value)
            }
        }

        return if (request.getMethodType() == MethodType.POST) {
            builder.post(createPostRequestBody(request)).url(request.getUrl()).build()
        }else {
            builder.url(createGetUrl(request.getUrl(), request.getParams())).build()
        }
    }

    override fun <T> get(request: HttpRequest): T? = mOkHttpClient.newCall(conversionRequest(request)).execute() as? T

    override fun <T> post(request: HttpRequest): T? = mOkHttpClient.newCall(conversionRequest(request)).execute() as? T

    override fun get(request: HttpRequest, callback: ResponseCallback?) = processRequest(request, callback)

    override fun post(request: HttpRequest, callback: ResponseCallback?) = processRequest(request, callback)

    override fun cancel(tag: String) {
        val queuedCalls = mOkHttpClient.dispatcher().queuedCalls()
        for (call in queuedCalls) {
            if (tag == call.request().tag()) {
                call.cancel()
            }
        }
        val runningCalls = mOkHttpClient.dispatcher().runningCalls()
        for (call in runningCalls) {
            if (tag == call.request().tag()) {
                call.cancel()
            }
        }
    }

    override fun cancelAll() {
        val queuedCalls = mOkHttpClient.dispatcher().queuedCalls()
        for (call in queuedCalls) {
            call.cancel()
        }
        val runningCalls = mOkHttpClient.dispatcher().runningCalls()
        for (call in runningCalls) {
            call.cancel()
        }
    }

    override fun download(request: HttpRequest, callback: DownloadCallback?) {
        request.getDownloadFileWrap()?.let {
            processDownload(request, callback)
        } ?: throw IllegalArgumentException("请使用DownloadFileBuilder对象创建request......")
    }

    override fun upload(request: HttpRequest, callback: UploadCallback?) {
        request.getUploadFileWrap()?.let {
            val builder = mOkHttpClient.newBuilder()
            builder.addInterceptor {
                val original = it.request()
                it.proceed(original.newBuilder().method(original.method(),
                        ProgressRequestBody(original.body()!!, callback)).build())
            }

            builder.build().newCall(conversionRequest(request)).enqueue(object : Callback{
                override fun onFailure(call: Call?, e: IOException) {
                    e.printStackTrace()
                    processFailure(e, callback)
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (null != response) {
                        if (response.isSuccessful) {
                            callback?.onSuccess(response.code(), response.body()?.string())
                        } else {
                            callback?.onError(response.code(), response.message())
                        }
                    } else {
                        callback?.onError(ErrorCode.CONNECT_ERROR.code, ErrorCode.CONNECT_ERROR.getErrorMsg())
                    }
                }
            })
        } ?: throw IllegalArgumentException("请使用UploadFileBuilder对象创建request......")
    }

    private val mOkHttpClient: OkHttpClient = OkHttpClient
            .Builder()
            .connectTimeout(10*1000, TimeUnit.MILLISECONDS)
            .readTimeout(10*1000,TimeUnit.MILLISECONDS)
            .writeTimeout(10*1000,TimeUnit.MILLISECONDS)
            .build()

    private val mOkhttpHelper: OkHttpHelper = OkHttpHelper(mOkHttpClient)

    override fun getHttpClient(): OkHttpClient = mOkHttpClient

    /**--------------------------------------------------------------------------------------------*/

    private fun createPostRequestBody(request: HttpRequest): RequestBody{
        val builder = FormBody.Builder()
        for ((k,v) in request.getParams()) {
            builder.add(k, v)
        }

        return request.getUploadFileWrap()?.let {
            val mBuilder = MultipartBody.Builder()
            mBuilder.setType(MultipartBody.FORM)
            mBuilder.addPart(builder.build())
            mBuilder.addFormDataPart("file", it.getFileName(),RequestBody.create(getMediaType(it.isImg), it.file))
            mBuilder.build()
        } ?: builder.build()
    }

    private fun getMediaType(isImg: Boolean): MediaType? {
        return if (isImg) {
            MediaType.parse("image/*")
        } else {
            MediaType.parse("application/octet-stream")
        }
    }

    private fun processRequest(request: HttpRequest, callback: ResponseCallback?){
        if (null != callback && callback is SimpleResponseCallback) {
            callback.onStart()
        }
        mOkHttpClient.newCall(conversionRequest(request)).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                processFailure(e, callback)
            }

            override fun onResponse(call: Call, response: Response) {
                if (null != response) {
                    if (response.isSuccessful) {
                        if (!request.isReturnByteArray()) {
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

    private fun processDownload(request: HttpRequest, callback: DownloadCallback?) {
        val client = mOkHttpClient.newBuilder().build()
        client.newCall(conversionRequest(request)).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                processFailure(e, callback)
            }

            override fun onResponse(call: Call, response: Response) {
                if (null != response) {
                    if (response.isSuccessful) {
                        if (null == request.getDownloadFileWrap()) {
                            callback?.onError(ErrorCode.DOWNLOAD_ERROR.code, ErrorCode.DOWNLOAD_ERROR.getErrorMsg())
                        } else {
                            if (downLoadSaveFile(response.body()!!.byteStream(),
                                            Integer.valueOf(response.header("Content-Length", "-1")).toLong(),
                                            request,
                                            callback)) {
                                callback?.onSuccess(response.code(), "download success......")
                            } else {
                                callback?.onError(ErrorCode.DOWNLOAD_ERROR.code, ErrorCode.DOWNLOAD_ERROR.getErrorMsg())
                            }

                        }
                    } else {
                        callback?.onError(response.code(), response.message())
                    }
                } else {
                    callback?.onError(ErrorCode.DOWNLOAD_ERROR.code, ErrorCode.DOWNLOAD_ERROR.getErrorMsg())
                }
            }
        })
    }
}