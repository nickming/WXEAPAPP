package com.wxeapapp.base

/**
 * class description here

 * @author nickming
 * *
 * @version 1.0.0
 * *
 * @since 2017-08-19 下午7:10
 * * Copyright (c) 2017 nickming All right reserved.
 */

interface BaseView<T : BasePresenter> {

    fun setPresenter(presenter: T)

    fun initViews()
}
