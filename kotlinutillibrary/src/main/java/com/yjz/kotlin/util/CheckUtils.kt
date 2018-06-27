package com.yjz.kotlin.util

/**
 * @author yjz
 * @Date 2018-06-27 23:14
 * 检查工具类
 */
object CheckUtils {

    /**
     * 检查字符串是否为空
     * @return true: 为空; false: 不为空
     */
    fun isEmpty(str: CharSequence): Boolean{
        return str.isNullOrEmpty();
    }
}