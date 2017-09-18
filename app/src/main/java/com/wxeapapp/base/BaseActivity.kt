package com.wxeapapp.base

import android.os.Bundle
import android.widget.Toast
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * class description here

 * @author nickming
 * *
 * @version 1.0.0
 * *
 * @since 2017-08-18 下午7:50
 * * Copyright (c) 2017 nickming All right reserved.
 */

open class BaseActivity : SwipeBackActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


    fun toast(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messageEvent: MessageEvent) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun closeSelf(closeActivityEvent: CloseActivityEvent){
        finish()
    }
}


