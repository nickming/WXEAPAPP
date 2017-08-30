package com.wxeapapp.model

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-25 上午9:44
 * Copyright (c) 2017 nickming All right reserved.
 */
data class PayLoad(val type: String, val payload: Item?) {

    data class Item(val title: String?, val url: String?, val error: String?,val imageData:String?)
}