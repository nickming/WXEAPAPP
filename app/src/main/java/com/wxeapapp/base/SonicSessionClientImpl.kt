package com.wxeapapp.base

import android.os.Bundle
import com.tencent.smtt.sdk.WebView
import com.tencent.sonic.sdk.SonicSessionClient
import java.util.*

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-18 下午8:27
 * Copyright (c) 2017 nickming All right reserved.
 */
class SonicSessionClientImpl : SonicSessionClient() {

    lateinit var webView: WebView

    fun bindWebView(webView: WebView) {
        this.webView = webView
    }


    override fun loadDataWithBaseUrlAndHeader(baseUrl: String?, data: String?, mimeType: String?, encoding: String?, historyUrl: String?, headers: HashMap<String, String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadUrl(url: String?, extraData: Bundle?) {
        webView.loadUrl(url)
    }

    override fun loadDataWithBaseUrl(baseUrl: String?, data: String?, mimeType: String?, encoding: String?, historyUrl: String?) {
        webView.loadDataWithBaseURL(baseUrl,data,mimeType,encoding,historyUrl)
    }
}