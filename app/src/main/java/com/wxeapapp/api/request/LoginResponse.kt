package com.wxeapapp.api.request

import java.io.Serializable

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-20 下午10:10
 * Copyright (c) 2017 nickming All right reserved.
 */
data class LoginResponse(val result: Int,
                         val errmsg: String,
                         val token: String,
                         val data: List<Item>) : Serializable {

    data class Item(val AemNo: String,
                    val AemName: String,
                    val ArgFullAddress: String,
                    val RegName: String,
                    val RegShortName: String) : Serializable {}
}