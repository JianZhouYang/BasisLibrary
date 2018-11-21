package com.yjz.support.imageselector.base;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.app.AppCompatActivity;
import com.yjz.support.imageselector.iface.ICamera;

public abstract class BaseSelector implements ICamera, LifecycleObserver {
    /**拍照请求码*/
    public static final int IMAGE_CAPTURE_REQUEST = 88;
    /**裁剪请求码*/
    public static final int IMAGE_CROP_REQUEST = 89;

    protected Activity mActivity;


    public BaseSelector(Activity activity){
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity)activity).getLifecycle().addObserver(this);
        }
        mActivity = activity;
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy(){
        if (null != mActivity && mActivity instanceof AppCompatActivity) {
            ((AppCompatActivity)mActivity).getLifecycle().removeObserver(this);
        }
        mActivity = null;
    }

}
