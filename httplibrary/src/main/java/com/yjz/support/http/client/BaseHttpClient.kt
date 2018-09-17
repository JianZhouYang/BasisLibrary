package com.yjz.support.http.client

import com.yjz.support.http.HttpRequest
import com.yjz.support.http.iface.IHttpClient

abstract class BaseHttpClient<out E, T> : IHttpClient<T> {
    protected abstract fun conversionRequest(request: HttpRequest): E
}