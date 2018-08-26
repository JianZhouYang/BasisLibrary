package com.yjz.http.client

import com.yjz.http.ErrorCode
import com.yjz.http.HttpRequest
import com.yjz.http.MethodType
import com.yjz.http.callback.DownloadCallback
import com.yjz.http.callback.ResponseCallback
import okhttp3.*
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class DefaultOkHttpClient : BaseHttpClient<Request, OkHttpClient>() {

    override fun conversionRequest(request: HttpRequest): Request {
        val builder = Request.Builder()
                .url(request.getUrl())
        if (null != request.getTag()) {
            builder.tag(request.getTag())
        }
        if (request.getMethodType() == MethodType.POST) {
            builder.post(createRequestBody(request))
        }
        if (request.getHeaders().isNotEmpty()) {
            request.getHeaders().forEach{
                builder.addHeader(it.key, it.value)
            }
        }
        return builder.build()
    }

    override fun <T> get(request: HttpRequest): T? = mOkHttpClient.newCall(conversionRequest(request)).execute() as? T

    override fun <T> post(request: HttpRequest): T? = mOkHttpClient.newCall(conversionRequest(request)).execute() as? T

    override fun get(request: HttpRequest, callback: ResponseCallback?) {
        mOkHttpClient.newCall(conversionRequest(request)).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                processFailure(e, callback)
            }

            override fun onResponse(call: Call, response: Response) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun post(request: HttpRequest, callback: ResponseCallback?) {
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
                                if (saveFile(response, request, callback)) {
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
        } ?: throw IllegalArgumentException("请使用DownloadFileBuilder对象创建request......")
    }

    private fun saveFile(response: Response, request: HttpRequest, callback: DownloadCallback?): Boolean {
        var isSuccess = true
        var inputStream: InputStream? = null
        var fos: FileOutputStream? = null
        var buf = ByteArray(100)
        var len = 0//本次读取的字节数
        var currSize: Long = 0//当前已经读取的字节数
        //总大小
        var totalSize = Integer.valueOf(response.header("Content-Length", "-1")).toLong()
        try {
            val file = request.getDownloadFileWrap()!!.file
            val dir = file.parentFile
            if (!dir.exists()) {
                dir.mkdirs()
            }
            fos = file.outputStream()
            inputStream = response.body()!!.byteStream() //获取返回的Stream
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

    private val mOkHttpClient: OkHttpClient = OkHttpClient
            .Builder()
            .connectTimeout(10*1000, TimeUnit.MILLISECONDS)
            .readTimeout(10*1000,TimeUnit.MILLISECONDS)
            .writeTimeout(10*1000,TimeUnit.MILLISECONDS)
            .build()

    private val mOkhttpHelper: OkHttpHelper = OkHttpHelper(mOkHttpClient)

    override fun getHttpClient(): OkHttpClient = mOkHttpClient

    /**--------------------------------------------------------------------------------------------*/

    private fun createRequestBody(request: HttpRequest): RequestBody{
        val builder = FormBody.Builder()
        for ((k,v) in request.getParams()) {
            builder.add(k, v)
        }
        return if (null != request.getUploadFileWrap()) {
            val mBuilder = MultipartBody.Builder()
            mBuilder.setType(MultipartBody.FORM)
            mBuilder.addPart(builder.build())
            mBuilder.addFormDataPart("file", request.getUploadFileWrap()!!.getFileName(),
                    RequestBody.create(getMediaType(request.getUploadFileWrap()!!.isImg), request.getUploadFileWrap()!!.file))
            mBuilder.build()
        } else {
            builder.build()
        }
    }

    private fun getMediaType(isImg: Boolean): MediaType? {
        return if (isImg) {
            MediaType.parse("image/*")
        } else {
            MediaType.parse("application/octet-stream")
        }
    }
}