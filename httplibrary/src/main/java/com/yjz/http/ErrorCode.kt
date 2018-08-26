package com.yjz.http

enum class ErrorCode(var code: Int) {
    TIME_OUT(4001) {
        override fun getErrorMsg(): String = "连接超时"
    },
    CONNECT_ERROR(4002) {
        override fun getErrorMsg(): String = "连接错误"
    },
    DOWNLOAD_ERROR(4003){
        override fun getErrorMsg(): String = "下载错误"
    };

    abstract fun getErrorMsg(): String
}