package com.wxeapapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tencent.bugly.Bugly
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
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this.applicationContext
        Bugly.init(applicationContext, "12b31ffe3a", false)
        ApiUtils.init(this)
        XgPushUtils.init(this, true)
        XgPushUtils.registerPushBindAccount(this, { token ->
            L("register success:" + token)
        })
    }
}