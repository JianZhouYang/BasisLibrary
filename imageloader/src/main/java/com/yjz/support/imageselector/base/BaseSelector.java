package com.yjz.support.imageselector.base;

import android.app.Activity;
import com.yjz.support.imageselector.callback.ImageCallback;
import com.yjz.support.imageselector.iface.ICamera;

public abstract class BaseSelector implements ICamera {
    /**拍照请求码*/
    public static final int IMAGE_CAPTURE_REQUEST = 88;
    /**裁剪请求码*/
    public static final int IMAGE_CROP_REQUEST = 89;

    protected Activity mActivity;
    protected ImageCallback mImageCallback;

    public BaseSelector(Activity activity){
        this(activity, null);
    }

    public BaseSelector(Activity activity, ImageCallback callback){
        mActivity = activity;
        mImageCallback = callback;
    }

    public void setImageCallback(ImageCallback callback){
        mImageCallback = callback;
    }
}
