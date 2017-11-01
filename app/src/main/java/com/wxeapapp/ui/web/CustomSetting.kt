package com.wxeapapp.ui.web

import android.os.Build
import android.webkit.WebView
import com.just.library.AgentWebSettings
import com.just.library.WebDefaultSettingsManager
import com.wxeapapp.EAPApplication
import com.wxeapapp.utils.VersionUtil

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-24 下午1:34
 * Copyright (c) 2017 nickming All right reserved.
 */
class CustomSetting : WebDefaultSettingsManager() {

    override fun toSetting(webView: WebView?): AgentWebSettings<*> {
        super.toSetting(webView)
        webSettings.userAgentString = webSettings.userAgentString + " wxeap-app/${VersionUtil.getVersionName(EAPApplication.instance)}"
//        getWebSettings().setUserAgent(getWebSettings().userAgentString + " wxeap-app/1.0")
        webSettings.blockNetworkImage = false//是否阻塞加载网络图片  协议http or https
        webSettings.allowFileAccess = false //允许加载本地文件html  file协议, 这可能会造成不安全 , 建议重写关闭
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.allowFileAccessFromFileURLs = false
        } //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.allowUniversalAccessFromFileURLs = false
        }//允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
        return this
    }

//    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
//    override fun toSetting(webView: WebView): WebSettings<*>? {
//        super.toSetting(webView)
//        webSettings.userAgentString = webSettings.userAgentString + " wxeap-app/1.0"
////        getWebSettings().setUserAgent(getWebSettings().userAgentString + " wxeap-app/1.0")
//        webSettings.blockNetworkImage = false//是否阻塞加载网络图片  协议http or https
//        webSettings.allowFileAccess = false //允许加载本地文件html  file协议, 这可能会造成不安全 , 建议重写关闭
//        webSettings.allowFileAccessFromFileURLs = false //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
//        webSettings.allowUniversalAccessFromFileURLs = false//允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
//        return this
//    }
}