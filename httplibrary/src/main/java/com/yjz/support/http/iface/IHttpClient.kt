package com.yjz.support.http.iface


interface IHttpClient<out T> : IHttpOperate {

    fun getRealHttpClient(): T

}