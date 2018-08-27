package com.yjz.http

import java.io.File

/**
 * 请求对象类
 */
class HttpRequest private constructor(private val mData: RequestData) {
    fun getUrl(): String? = mData.mUrl

    fun getMethodType(): MethodType = mData.mMethodType

    fun getTag(): Any? = mData.mTag

    fun isReturnByteArray(): Boolean = mData.mIsReturnByteArray

    fun getHeaders(): MutableMap<String, String> = mData.mHeaders

    fun getParams(): MutableMap<String, String> = mData.mParams

    fun getUploadFileWrap(): FileWrap? = mData.mUploadFileWrap

    fun getDownloadFileWrap(): FileWrap? = mData.mDownloadFileWrap

    open class Builder{
        private val data = RequestData()

        protected open fun setFile(isImg: Boolean, file: File): Builder{
            data.mUploadFileWrap = FileWrap(isImg, file)
            return this
        }

        protected open fun saveFile(file: File): Builder{
            data.mDownloadFileWrap = FileWrap(false, file)
            return this
        }

        /**
         * 设置请求地址
         */
        fun setUrl(url: String): Builder{
            data.mUrl = url
            return this
        }

        /**
         * 设置请求类型
         * POST:post请求 GET:get请求；默认post请求
         */
        fun setMethodType(type: MethodType): Builder{
            data.mMethodType = type
            return this
        }

        fun setTag(tag: Any): Builder{
            data.mTag = tag
            return this
        }

        /**
         * 设置返回结果是否是字节数组
         * true: 请求结果返回字节数据; false: 请求结果不返回字节数组;
         * 默认为false
         */
        fun setReturnBtyeArray(isReturn: Boolean): Builder{
            data.mIsReturnByteArray = isReturn
            return this
        }

        fun addHeader(key: String, value: String): Builder {
            data.mHeaders[key] = value
            return this
        }

        /**
         * 添加参数
         */
        fun put(key: String, value: String): Builder{
            data.mParams[key] = value
            return this
        }

        fun create(): HttpRequest = HttpRequest(data)
    }



    class UploadFileBuilder(isImg: Boolean, file: File) : Builder(){
        init {
            super.setFile(isImg, file)
        }
    }

    class DownloadFileBuilder(file: File) : Builder(){
        init {
            setMethodType(MethodType.GET)
            super.saveFile(file)
        }
    }
}

private class RequestData{
    var mUrl: String? = null
    var mMethodType: MethodType = MethodType.POST
    var mTag: Any? = null
    var mIsReturnByteArray: Boolean = false
    val mHeaders: MutableMap<String, String> = mutableMapOf()
    val mParams: MutableMap<String, String> = mutableMapOf()
    var mUploadFileWrap: FileWrap? = null
    var mDownloadFileWrap: FileWrap? = null
}


class FileWrap(val isImg: Boolean, val file: File){
    fun getFileName(): String{
        return file.name
    }

     fun getSourceFile() = file
}

enum class MethodType{
    GET,
    POST
}