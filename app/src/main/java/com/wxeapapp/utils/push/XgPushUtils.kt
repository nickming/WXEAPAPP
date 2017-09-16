package com.wxeapapp.utils.push

import android.content.Context
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushConfig
import com.tencent.android.tpush.XGPushManager
import com.wxeapapp.utils.java.SPUtil

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-22 下午1:15
 * Copyright (c) 2017 nickming All right reserved.
 */
object XgPushUtils {
    //初始配置
    fun init(applicationContext: Context, debug: Boolean) {
        val accessId: Long = 2100263487
        val accessKey: String = "AQ2MNK87D27T"
        XGPushConfig.enableDebug(applicationContext, debug)
        XGPushConfig.setAccessId(applicationContext, accessId)
        XGPushConfig.setAccessKey(applicationContext, accessKey)
    }

    //注册信鸽推送并绑定账号
    fun registerPushBindAccount(applicationContext: Context, success: (token: String) -> Unit) {
        XGPushManager.registerPush(applicationContext, object : XGIOperateCallback {
            override fun onSuccess(data: Any?, flag: Int) {

                if (flag == XGPushManager.OPERATION_SUCCESS) {
                    SPUtil.put(applicationContext, SPUtil.CLIENT_ID, data as String)
                    success.invoke(data as? String ?: return)
                }
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                println()
            }
        })

    }
}