package com.yjz.support.imageselector;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import com.yjz.support.imageselector.base.BaseSelector;
import com.yjz.support.imageselector.base.FileItem;
import com.yjz.support.imageselector.callback.ImageCallback;
import com.yjz.support.util.CameraUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageSelector extends BaseSelector {

    private final String DIR_NAME_KEY = "dir_name_key";

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
    private ImageCallback mImageCallback;
    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks;

    public ImageSelector(Activity activity){
        this(activity, null);
    }

    public ImageSelector(Activity activity, ImageCallback callback){
        super(activity);

        mImageCallback = callback;
        mLoaderManager = ((FragmentActivity)activity).getSupportLoaderManager();
        mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                if (null != mImageCallback) {
                    mImageCallback.onQueryStart();
                }

                if (id == ALL_IMAGES) {
                    return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            null, null, IMAGE_PROJECTION[2] + " DESC");
                } else if (id == DIR_IMAGES) {
                    String dirName = args.getString(DIR_NAME_KEY);
                    return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                            IMAGE_PROJECTION[0] + " like '%" + dirName + "%'", null,
                            IMAGE_PROJECTION[2] + " DESC");
                }

                return null;
            }

            @Override
            public void onLoadFinished(@NonNull final Loader<Cursor> loader, final Cursor cursor) {
                new Thread() {
                    public void run() {
                        ArrayList<FileItem> list = new ArrayList<>();
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

                        if (null != mImageCallback) {
                            if (loader.getId() == ALL_IMAGES) {
                                mImageCallback.onQueryFinish(ImageCallback.Type.TYPE_ALL_IMAGES, list);
                            } else {
                                mImageCallback.onQueryFinish(ImageCallback.Type.TYPE_DIR_IMAGES, list);
                            }
                        }
                    }
                }.start();
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        };
    }

    public void setImageCallback(ImageCallback callback){
        mImageCallback = callback;
    }

    private void getImages(int id, final String dirName){
        Bundle bundle = null;
        if (id == DIR_IMAGES) {
            bundle = new Bundle();
            bundle.putString(DIR_NAME_KEY, !TextUtils.isEmpty(dirName) ? dirName : "");
        }
        mLoaderManager.restartLoader(id, bundle, mLoaderCallbacks);
    }

    public void getAllImages() {
        getImages(ALL_IMAGES, null);
    }

    public List<String> getImageDirNames() {
        return null;
    }

    public void getImagesByDirName(String dirName) {
        getImages(DIR_IMAGES, dirName);
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


    @Override
    public void destroy() {
        super.destroy();
        if (null != mLoaderManager) {
            mLoaderManager.destroyLoader(ALL_IMAGES);
            mLoaderManager.destroyLoader(DIR_IMAGES);
        }
        mLoaderManager = null;
    }
}
