package com.yjz.support.imageselector.base

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache

class MemoryCache {
    private val MAX_MEMORY_CACHE = (Runtime.getRuntime().maxMemory() / 1024 / 4).toInt()
    private val mMemoryCache: LruCache<String, Bitmap>

    init {
        mMemoryCache = object : LruCache<String, Bitmap>(MAX_MEMORY_CACHE){
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount / 1024
            }

            override fun entryRemoved(evicted: Boolean, key: String?, oldValue: Bitmap?, newValue: Bitmap?) {
                oldValue?.recycle()
            }
        }
    }

    fun clear(){
        mMemoryCache.evictAll()
    }

    fun getBitmapFromMemCache(key: String): Bitmap{
        addBitmapToMemoryCache(key)
        return mMemoryCache.get(key)
    }

    private fun addBitmapToMemoryCache(key: String) {
        if (mMemoryCache.get(key) == null) {
            mMemoryCache.put(key, createBitmap(key))
        }
    }

    private fun createBitmap(path: String): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        val toWidth = 100
        val toHeight = options.outHeight * toWidth / options.outWidth
        options.inSampleSize = 4
        options.outWidth = toWidth
        options.outHeight = toHeight
        options.inPreferredConfig = Bitmap.Config.ARGB_4444
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(path, options)
    }
}
