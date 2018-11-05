package com.yjz.support.util

import java.io.File

/**
 * @author yjz
 * @Date 2018-06-27 23:14
 * File操作工具类
 */
object FileUtils {


    /**
     * 删除文件
     * @return true: 删除成功；false: 删除失败
     */
    public fun delete(filePath: String?): Boolean{
        return if (!filePath.isNullOrEmpty()) {
            delete(File(filePath));
        } else {
            false;
        }
    }

    /**
     * 删除文件
     * @return true: 删除成功；false: 删除失败
     */
    public fun delete(file: File?): Boolean{
        return if (null != file && file.exists()) {
            if (!file.isDirectory) {
                file.delete();
            } else {
                val files = file.listFiles();
                if (null != files) {
                    for (i in files.indices) {
                        delete(files[i]);
                    }
                }
                file.delete();
            }
        } else {
            false;
        }
    }


    /**
     * 文件目录是否存在
     * @return true:存在 false:不存在
     */
    public fun isDirectoryExists(filePath: String): Boolean{
        return if (!CheckUtils.isEmpty(filePath)) {
            isDirectoryExists(File(filePath))
        } else {
            false
        }
    }

    /**
     * 文件目录是否存在
     * @return true:存在 false:不存在
     */
    public fun isDirectoryExists(file: File): Boolean{
        return if (null != file) {
            if (file.isDirectory) {
                file.exists()
            } else {
                file.parentFile?.exists() ?: false
            }
        } else {
            false
        }
    }
}