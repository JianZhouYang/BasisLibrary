package com.yjz.support.http.client

import com.yjz.support.http.callback.UploadCallback
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

class ProgressRequestBody(private val requestBody: RequestBody, private val callback: UploadCallback?) : RequestBody() {
    override fun contentType(): MediaType? = requestBody.contentType()

    override fun contentLength(): Long = requestBody.contentLength()

    override fun writeTo(sink: BufferedSink) {
        val bufferedSink = Okio.buffer(CountBufferedSink(sink, contentLength(), callback))
        requestBody.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    private class CountBufferedSink(val sink: Sink, val contentLength: Long, private val callback: UploadCallback?) : ForwardingSink(sink){
        private var writeCount: Long = 0L

        override fun write(source: Buffer?, byteCount: Long) {
            super.write(source, byteCount)
            callback?.let {
                writeCount += byteCount
                callback.onProgress(writeCount, contentLength, writeCount == contentLength)
            }
        }
    }
}