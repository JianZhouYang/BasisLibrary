package com.yjz.kotlin.util

import android.support.v4.util.SimpleArrayMap
import org.jetbrains.annotations.NotNull

/**
 * @author yjz
 * 用于管理activity
 */
public class LocalActivityManager {
    private val mLock: ByteArray = ByteArray(0);
    val mListenerMap: SimpleArrayMap<String, DestroyCallback> = SimpleArrayMap();

    fun destroyActivities(){
        synchronized(mLock){
            val size: Int = mListenerMap.size();
            for (i in 0..size) {
                mListenerMap.get(i).destroyAty()
            }
            mListenerMap.clear();
        }
    }

    fun setDestroyCallback(key: String, @NotNull callback: DestroyCallback){
        synchronized(mLock){
            mListenerMap.put(key, callback);
        }
    }

    fun removeDestroyCallback(key: String){
        synchronized(mLock){
            mListenerMap.remove(key);
        }
    }

     interface DestroyCallback {
        fun destroyAty();
    }
}