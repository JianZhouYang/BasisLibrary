package com.yjz.http

import java.net.URLEncoder

/**
 * 将传递进来的参数拼接成 url
 *
 * @param url
 * @param params
 * @return
 */
internal fun createGetUrl(url: String?, params: Map<String, String>): String {
    return if (null != url) {
        val sb = StringBuilder()
        sb.append(url)
        if (url.indexOf('&') > 0 || url.indexOf('?') > 0) {
            sb.append("&")
        } else {
            sb.append("?")
        }

        for ((key, value) in params) {
            val urlValue = URLEncoder.encode(value, "UTF-8")
            sb.append(key).append("=").append(urlValue).append("&")
        }
        sb.deleteCharAt(sb.length - 1)
        sb.toString()
    } else {
        ""
    }
}