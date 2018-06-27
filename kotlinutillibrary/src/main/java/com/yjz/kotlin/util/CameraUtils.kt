package com.yjz.kotlin.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File

object CameraUtils {
    /**启动系统相机请求码 */
    const val PHOTO_GRAPH_REQUEST = 110
    /**启动系统照片裁剪器请求码 */
    const val PHOTO_CUTTING_REQUEST = 111

    /**
     * 相机拍照
     * @param activity
     * @param savePath 图片保存的完整路径 eg:/storage/sdcard0/Android/data/com.yjz.test(包名)/temp/test.jpg
     * @param requestCode
     */
    fun photograph(activity: Activity, savePath: String, requestCode: Int){
        val file = checkFile(savePath);

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        val uri: Uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val packageName = activity.packageName;
            uri = FileProvider.getUriForFile(activity, "$packageName.fileProvider", file);
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
     * @param srcFile
     * @param savePath
     * @param width
     * @param height
     * @param requestCode
     */
    fun cutting(activity: Activity, srcFile: File, savePath: String, width: Int, height: Int, requestCode: Int){
        val saveFile = checkFile(savePath);
        val intent = Intent("com.android.camera.action.CROP");
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val packageName = activity.packageName;
            uri = FileProvider.getUriForFile(activity, "$packageName.fileProvider", srcFile);
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile));
        activity.startActivityForResult(intent, requestCode)
    }

    private fun checkFile(savePath: String): File {
        val file = File(savePath);
        if (!FileUtils.isDirectoryExists(file)) {
            if (null != file.parentFile) {
                file.parentFile.mkdirs();
            }
        }
        return file
    }
}