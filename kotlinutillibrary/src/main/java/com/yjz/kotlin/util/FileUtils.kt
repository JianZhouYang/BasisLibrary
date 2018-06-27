package com.yjz.kotlin.util

import java.io.File

/**
 * @author yjz
 * @Date 2018-06-27 23:14
 * File操作工具类
 */
object FileUtils {

    fun isDirectoryExists(filePath: String): Boolean{
        if (CheckUtils.isEmpty(filePath)) return false;
        return isDirectoryExists(File(filePath));
    }

    fun isDirectoryExists(file: File): Boolean{
        if (null == file) return false;
        if (file.isDirectory) {
            return file.exists();
        } else {
            val parentFile = file.parentFile ?: return false;
            return parentFile.exists();
        }
    }
}