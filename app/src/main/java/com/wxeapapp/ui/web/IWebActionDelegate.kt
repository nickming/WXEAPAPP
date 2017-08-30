package com.wxeapapp.ui.web

import com.wxeapapp.model.PayLoad

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-25 下午8:00
 * Copyright (c) 2017 nickming All right reserved.
 */
interface IWebActionDelegate {

    fun onShouldPush(payLoad: PayLoad): Unit
    fun onTitleUpdate(payLoad: PayLoad): Unit
    fun onGoBack(payLoad: PayLoad): Unit
    fun onShowImagePicker(payLoad: PayLoad): Unit
    fun onSwitchSystem(payLoad: PayLoad): Unit
    fun onLogout(payLoad: PayLoad): Unit
}