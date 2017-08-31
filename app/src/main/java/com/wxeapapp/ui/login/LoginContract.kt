package com.wxeapapp.ui.login

import com.wxeapapp.api.request.LoginResponse
import com.wxeapapp.base.BasePresenter
import com.wxeapapp.base.BaseView


/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-19 下午7:06
 * Copyright (c) 2017 nickming All right reserved.
 */
interface LoginContract {

    interface View : BaseView<Presenter> {
        fun showToast(string: String)

        fun showDone()

        fun showStart()

        fun showHelpDialog()

        fun showTimeTvState(enable: Boolean, text: String)

        fun jumpToWeb(loginResponse: LoginResponse)

        fun requestAllPermission()

        fun autoLogin()

        fun showLoginEnable(enable: Boolean)

        fun closeSelf()

        fun showSwitchSystem()

        fun showLoadingCompany()

        fun hideLoadingCompany()
    }

    interface Presenter : BasePresenter {
        fun login(map: HashMap<String, String>)

        fun requestVerifyCode(mobile: String)
    }
}