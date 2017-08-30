package com.wxeapapp.utils.push

import android.content.Context
import android.content.Intent
import com.tencent.android.tpush.*
import com.wxeapapp.ui.login.LoginActivity

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-22 下午1:15
 * Copyright (c) 2017 nickming All right reserved.
 */
class XgPushReceiver : XGPushBaseReceiver() {


    //设置标签结果
    override fun onSetTagResult(p0: Context?, p1: Int, p2: String?) {
        println()
    }

    //通知被展示触发的结果，可以在此保存APP收到的通知
    override fun onNotifactionShowedResult(p0: Context?, p1: XGPushShowedResult?) {
        println()
    }

    //反注册结果
    override fun onUnregisterResult(p0: Context?, p1: Int) {
        println()
    }

    //删除标签结果
    override fun onDeleteTagResult(p0: Context?, p1: Int, p2: String?) {
        println()
    }

    //注册结果
    override fun onRegisterResult(p0: Context?, p1: Int, p2: XGPushRegisterResult?) {
        println()
    }

    //收到消息
    override fun onTextMessage(p0: Context?, p1: XGPushTextMessage?) {
        println()
    }

    //通知被打开触发的结果
    override fun onNotifactionClickedResult(p0: Context, p1: XGPushClickedResult) {
        if (p1.notificationActionType == XGPushClickedResult.NOTIFICATION_ACTION_ACTIVITY
                && p1.actionType.toInt() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            p0.startActivity(Intent(p0, LoginActivity::class.java))
            //仅App在后台时才启动
//            router(p0, RouterImpl.SplashActivity, Pair("pushContent", p1.customContent))
        }
    }

}