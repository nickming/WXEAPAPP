package com.wxeapapp.base

import android.content.Context
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.CookieManager
import com.tencent.sonic.sdk.SonicRuntime
import com.tencent.sonic.sdk.SonicSessionClient
import java.io.File
import java.io.InputStream


/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-18 下午8:22
 * Copyright (c) 2017 nickming All right reserved.
 */
class HostSonicRuntime(context: Context) : SonicRuntime(context) {


    override fun isSonicUrl(url: String?): Boolean {
        return true
    }

    override fun showToast(text: CharSequence?, duration: Int) {

    }

    override fun setCookie(url: String?, cookies: MutableList<String>?): Boolean {
        if (!TextUtils.isEmpty(url) && cookies != null && cookies.size > 0) {
            val cookieManager = CookieManager.getInstance()
            for (cookie in cookies) {
                cookieManager.setCookie(url, cookie)
            }
            return true
        }
        return false
    }

    override fun getCookie(url: String?): String {
        val cookieManager = CookieManager.getInstance()
        return cookieManager.getCookie(url)
    }

    override fun log(tag: String?, level: Int, message: String?) {

    }

    override fun getUserAgent(): String {
        return ""
    }

    override fun createWebResourceResponse(mimeType: String?, encoding: String?, data: InputStream?, headers: MutableMap<String, String>?): Any {
        val resourceResponse = WebResourceResponse(mimeType, encoding, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resourceResponse.setResponseHeaders(headers)
        }
        return resourceResponse
    }

    override fun isNetworkValid(): Boolean {
        return true;
    }

    override fun getCurrentUserAccount(): String {
        return ""
    }

    override fun postTaskToThread(task: Runnable?, delayMillis: Long) {
        val thread = Thread(task, "SonicThread")
        thread.start()
    }

    override fun notifyError(client: SonicSessionClient?, url: String?, errorCode: Int) {

    }

    override fun getSonicCacheDir(): File {
        val path = Environment.getExternalStorageDirectory().absolutePath + File.separator
        val file = File(path)
        if (!file.exists()) {
            file.mkdir()
        }
        return file
    }

}