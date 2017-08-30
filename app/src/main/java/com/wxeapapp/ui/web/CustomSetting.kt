package com.wxeapapp.ui.web

import android.os.Build
import android.support.annotation.RequiresApi
import com.just.agentwebX5.WebDefaultSettingsManager
import com.tencent.smtt.sdk.WebView

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-24 下午1:34
 * Copyright (c) 2017 nickming All right reserved.
 */
class CustomSetting : WebDefaultSettingsManager() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun toSetting(webView: WebView): com.just.agentwebX5.WebSettings<*>? {
        super.toSetting(webView)
        getWebSettings().setUserAgent(getWebSettings().userAgentString + " wxeap-app/1.0")
        getWebSettings().setBlockNetworkImage(false)//是否阻塞加载网络图片  协议http or https
        getWebSettings().setAllowFileAccess(false) //允许加载本地文件html  file协议, 这可能会造成不安全 , 建议重写关闭
        getWebSettings().setAllowFileAccessFromFileURLs(false) //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
        getWebSettings().setAllowUniversalAccessFromFileURLs(false)//允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
        return this
    }
}