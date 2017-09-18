package com.wxeapapp

import android.app.Application
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
        lateinit var instance: Application


    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
//        CrashReport.initCrashReport(applicationContext, "12b31ffe3a", true)
        Bugly.init(applicationContext, "12b31ffe3a", false)
        ApiUtils.init(this)
        XgPushUtils.init(this, true)
        XgPushUtils.registerPushBindAccount(this, {
            token ->
            L("register success:" + token)
        })
//        val appVersion = packageManager.getPackageInfo(packageName, 0).versionName
//        // initialize最好放在attachBaseContext最前面
//        SophixManager.getInstance().setContext(this)
//                .setAppVersion(appVersion)
//                .setAesKey(null)
//                .setEnableDebug(true)
//                .setPatchLoadStatusStub { mode, code, info, handlePatchVersion ->
//                    // 补丁加载回调通知
//                    if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//                        L("补丁加载成功!")
//                        // 表明补丁加载成功
//                    } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
//                        L("补丁需要重启生效")
//                        // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
//                        // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
//                    } else {
//                        // 其它错误信息, 查看PatchStatus类说明
//                    }
//                }.initialize()
//        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
//        SophixManager.getInstance().queryAndLoadNewPatch()

    }
}