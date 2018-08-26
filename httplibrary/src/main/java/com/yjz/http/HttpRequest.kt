package com.yjz.http

import java.io.File

/**
 * 请求对象类
 */
class HttpRequest constructor(private val mBuilder: Builder) {
    fun getUrl(): String? = mBuilder.mUrl;

    fun getMethodType(): MethodType = mBuilder.mMethodType;

    fun getTag(): Any? = mBuilder.mTag

    fun isReturnByteArray(): Boolean = mBuilder.mIsReturnByteArray

    fun getHeaders(): MutableMap<String, String> = mBuilder.mHeaders

    fun getParams(): MutableMap<String, String> = mBuilder.mParams

    fun getUploadFileWrap(): FileWrap? = mBuilder.mUploadFileWrap

    fun getDownloadFileWrap(): FileWrap? = mBuilder.mDownloadFileWrap

    open class Builder{
        internal var mUrl: String? = null
        internal var mMethodType: MethodType = MethodType.POST
        internal var mTag: Any? = null
        internal var mIsReturnByteArray: Boolean = false
        internal val mHeaders: MutableMap<String, String> = mutableMapOf()
        internal val mParams: MutableMap<String, String> = mutableMapOf()
        internal var mUploadFileWrap: FileWrap? = null
        internal var mDownloadFileWrap: FileWrap? = null

        protected open fun setFile(isImg: Boolean, file: File): Builder{
            mUploadFileWrap = FileWrap(isImg, file)
            return this
        }

        protected open fun saveFile(file: File): Builder{
            mDownloadFileWrap = FileWrap(false, file)
            return this
        }

        /**
         * 设置请求地址
         */
        fun setUrl(url: String): Builder{
            mUrl = url
            return this
        }

        /**
         * 设置请求类型
         * POST:post请求 GET:get请求；默认post请求
         */
        fun setMethodType(type: MethodType): Builder{
            mMethodType = type
            return this
        }

        fun setTag(tag: Any): Builder{
            mTag = tag
            return this
        }

        /**
         * 设置返回结果是否是字节数组
         * true: 请求结果返回字节数据; false: 请求结果不返回字节数组;
         * 默认为false
         */
        fun setReturnBtyeArray(isReturn: Boolean): Builder{
            mIsReturnByteArray = isReturn
            return this
        }

        fun addHeader(key: String, value: String): Builder {
            mHeaders[key] = value
            return this
        }

        /**
         * 添加参数
         */
        fun put(key: String, value: String): Builder{
            mParams[key] = value
            return this
        }

        fun create(): HttpRequest = HttpRequest(this)
    }



    class UploadFileBuilder : Builder(){
        public override fun setFile(isImg: Boolean, file: File): Builder {
            return super.setFile(isImg, file)
        }
    }

    class DownloadFileBuilder : Builder(){
        public override fun saveFile(file: File): Builder {
            return super.saveFile(file)
        }
    }
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