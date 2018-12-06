package com.yjz.support.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.util.ArrayMap
import org.jetbrains.annotations.NotNull

/**
 * 本地广播工具类
 */
object LocalBroadcast {
    private val DATA_KEY = "data.key"
    private val INTEREST_ACTION_KEY = "interest.action.key"
    private val BROADCAST_ACTION = "broadcast.action.local"

    private val mLock: ByteArray = ByteArray(0)
    private lateinit var mManager: LocalBroadcastManager
    private val mMap = ArrayMap<String, InterestListener>()

    @MainThread
    fun init(context: Context){
        if (null == mManager) {
            mManager = LocalBroadcastManager.getInstance(context)
        } else {
            IllegalArgumentException("无法重复初始化！")
        }
        val filter = IntentFilter()
        filter.addAction(BROADCAST_ACTION)
        mManager.registerReceiver(LocalBroadcastReceiver(), filter)

    }


    /**
     * 注册监听
     * @param key
     * @param listener
     */
    @MainThread
    fun register(@NotNull key: String, @NotNull listener: InterestListener) {
        check()
        synchronized(mLock) {
            mMap.put(key, listener)
        }
    }

    /**
     * 取消监听
     * @param key
     */
    @MainThread
    fun unregister(@NotNull key: String) {
        check()
        synchronized(mLock) {
            mMap.remove(key)
        }
    }

    /**
     * 发送广播消息
     * @param action 动作码
     * @param bundle 携带的数据(可为null)
     */
    fun sendBroadcastMsg(@NotNull action: String, bundle: Bundle?) {
        check()
        mManager.sendBroadcast(createIntent(action, bundle))
    }

    fun sendBroadcastMsg(@NotNull action: String) {
        sendBroadcastMsg(action, null)
    }

    private fun createIntent(action: String, bundle: Bundle?): Intent {
        val intent = Intent()
        intent.action = BROADCAST_ACTION
        intent.putExtra(INTEREST_ACTION_KEY, action)
        bundle?.let { intent.putExtra(DATA_KEY, bundle) }
        return intent
    }

    private fun check(){
        synchronized(mLock){
            if (null == mManager) {
                IllegalArgumentException("请先调用init()方法初始化！")
            }
        }
    }


    private class LocalBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            synchronized(mLock) {
                for (listener in mMap.values) {
                    listener.onAccept(intent.getStringExtra(INTEREST_ACTION_KEY), intent.getBundleExtra(DATA_KEY))
                }
            }
        }
    }

    interface InterestListener{
        fun onAccept(action: String, bundle: Bundle)
    }
}