package com.wxeapapp.utils

import android.util.Log

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-20 上午7:57
 * Copyright (c) 2017 nickming All right reserved.
 */
class Constant {
    companion object {
        val PARAM_URL = "param_url"
        val DEFAULT_URL = "/WxAppAuth.aspx?url=default.aspx"
        val WEB_MODE = "WEB_MODE"
    }
}

inline fun <reified T> T.L(message: Any, tag: String = T::class.java.simpleName) {
    Log.i(tag, message as String?)
}