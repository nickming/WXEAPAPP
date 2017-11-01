package com.wxeapapp.utils

import android.content.Context

/**
 * Created by nickming on 01/11/2017.
 */
object VersionUtil {

    fun getVersionName(context: Context):String{
        val info=context.packageManager.getPackageInfo(context.packageName,0)
        return info.versionName
    }
}