package com.yjz.support.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File

/**
 * @author yjz
 * @Date 2018-06-27 23:14
 * 调用系统相机拍照以及系统图片裁剪器的工具类
 */
object CameraUtils {
    /**启动系统相机请求码 */
    const val PHOTO_GRAPH_REQUEST = 110
    /**启动系统照片裁剪器请求码 */
    const val PHOTO_CUTTING_REQUEST = 111

    /**
     * 相机拍照
     * @param activity
     * @param savePath 图片保存的完整路径 eg:/storage/sdcard0/Android/data/com.yjz.test(包名)/temp/test.jpg
     * @param fileProvider eg: packageName + ".fileProvider"
     * @param requestCode
     */
    fun capture(activity: Activity, savePath: String, fileProvider: String, requestCode: Int){
        val file = File(savePath)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        val uri: Uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, fileProvider, file);
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动照片裁剪
     * @param activity
     * @param srcFile 源图片文件
     * @param savePath 图片裁剪后保存路径
     * @param width 裁剪图片宽
     * @param height 裁剪图片高
     * @param fileProvider eg: packageName + ".fileProvider"
     * @param requestCode
     */
    fun crop(activity: Activity, srcFile: File, savePath: String, width: Int, height: Int,
             fileProvider: String, requestCode: Int){
        val intent = Intent("com.android.camera.action.CROP");
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, fileProvider, srcFile);
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        } else {
            uri = Uri.fromFile(srcFile);
        }
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(savePath)));
        activity.startActivityForResult(intent, requestCode)
    }
}