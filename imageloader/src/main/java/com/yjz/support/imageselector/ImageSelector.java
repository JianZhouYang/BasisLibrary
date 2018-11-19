package com.yjz.support.imageselector;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import com.yjz.support.imageselector.callback.ImageCallback;
import com.yjz.support.imageselector.iface.ICamera;
import com.yjz.support.imageselector.iface.IImageSelect;
import com.yjz.support.util.CameraUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageSelector implements IImageSelect, ICamera, LifecycleObserver {
    /**拍照请求码*/
    public static final int IMAGE_CAPTURE_REQUEST = 88;
    /**裁剪请求码*/
    public static final int IMAGE_CROP_REQUEST = 89;

    private final int ALL_IMAGES = 0;
    private final int DIR_IMAGES = 1;

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.SIZE
    };

    private String lastCapturePath;
    private String lastCropPath;
    private LoaderManager mLoaderManager;
    private Activity mActivity;

    public ImageSelector(Activity activity){
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity)activity).getLifecycle().addObserver(this);
        }
        mActivity = activity;
        mLoaderManager = ((FragmentActivity)activity).getSupportLoaderManager();
    }

    private void getImages(int id, final String dirName, final ImageCallback callback){
        mLoaderManager.restartLoader(id, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                if (null != callback) {
                    callback.onQueryStart();
                }

                if (id == ALL_IMAGES) {
                    return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            null, null, IMAGE_PROJECTION[2] + " DESC");
                } else if (id == DIR_IMAGES) {
                    return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            IMAGE_PROJECTION[0] + " like '%" + dirName + "%'", null,
                            IMAGE_PROJECTION[2] + " DESC");
                }

                return null;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, final Cursor cursor) {
                new Thread(){
                    public void run(){
                        ArrayList<FileItem> list = new ArrayList<>();
                        if (cursor != null) {
                            if (cursor.getCount() != 0) {
                                while (cursor.moveToNext()) {
                                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                                    String fileName =
                                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                                    String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                                    String date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));

                                    FileItem item = new FileItem(path, fileName, size, date);
                                    list.add(item);
                                }
                            }
                            cursor.close();
                        }

                        if (null != callback) {
                            callback.onQueryFinish(list);
                        }
                    }
                }.start();
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
    }


    @Override
    public void getAllImages(ImageCallback callback) {
        getImages(ALL_IMAGES, null, callback);
    }

    @Override
    public List<String> getImageDirNames() {
        return null;
    }

    @Override
    public void getImagesByDirName(String dirName, ImageCallback callback) {
        getImages(DIR_IMAGES, dirName, callback);
    }

    @Override
    public void capture(Activity ac, String savePath, String fileProvider, int requestCode) {
        lastCapturePath = savePath;
        CameraUtils.INSTANCE.capture(ac, savePath, fileProvider, requestCode);
    }

    @Override
    public String getLastCapturePath() {
        return lastCapturePath;
    }

    @Override
    public void crop(Activity ac, File srcFile, String savePath, int width, int height, String fileProvider, int requestCode) {
        lastCropPath = savePath;
        CameraUtils.INSTANCE.crop(ac, srcFile, savePath, width, height, fileProvider, requestCode);
    }

    @Override
    public String getLastCropPath() {
        return lastCropPath;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy(){
        if (null != mLoaderManager) {
            mLoaderManager.destroyLoader(ALL_IMAGES);
            mLoaderManager.destroyLoader(DIR_IMAGES);
        }
        if (mActivity instanceof AppCompatActivity) {
            ((AppCompatActivity)mActivity).getLifecycle().addObserver(this);
        }
        mLoaderManager = null;
        mActivity = null;
    }
}
