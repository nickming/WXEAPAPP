package com.wxeapapp

import android.app.Application
import com.tencent.smtt.sdk.QbSdk
import com.wxeapapp.api.ApiUtils
import com.wxeapapp.utils.L
import com.wxeapapp.utils.push.XgPushUtils

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-21 下午10:02
 * Copyright (c) 2017 nickming All right reserved.
 */
class EAPApplication : Application() {

    companion object {
        lateinit var instance: Application
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        ApiUtils.init(this)
        QbSdk.initX5Environment(this, null)
        XgPushUtils.init(this, true)
        XgPushUtils.registerPushBindAccount(this, {
            token ->
            L("register success:" + token)
        })

    }
}