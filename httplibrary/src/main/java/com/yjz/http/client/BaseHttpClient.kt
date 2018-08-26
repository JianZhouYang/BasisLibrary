package com.yjz.http.client

import com.yjz.http.HttpRequest
import com.yjz.http.iface.IHttpClient

abstract class BaseHttpClient<out E, T> : IHttpClient<T> {
    protected abstract fun conversionRequest(request: HttpRequest): E
}