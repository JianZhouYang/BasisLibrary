package com.yjz.http.iface

interface IHttpClient<out T> : IHttpOperate {

    fun getHttpClient(): T;

}