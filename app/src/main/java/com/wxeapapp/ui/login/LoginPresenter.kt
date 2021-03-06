package com.wxeapapp.ui.login

import android.os.CountDownTimer
import com.google.gson.Gson
import com.wxeapapp.EAPApplication
import com.wxeapapp.api.LoginApi
import com.wxeapapp.utils.L
import com.wxeapapp.utils.java.NetWorkUtil
import com.wxeapapp.utils.java.SPUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody


/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-20 上午8:55
 * Copyright (c) 2017 nickming All right reserved.
 */
class LoginPresenter(val mView: LoginContract.View) : LoginContract.Presenter {


    var countDown: CountDownTimer


    init {
        countDown = object : CountDownTimer(60 * 1000, 1000) {
            override fun onFinish() {
                mView.showTimeTvState(true, "再次发送验证码")
            }

            override fun onTick(p0: Long) {
                mView.showTimeTvState(false, "重新发送(${p0 / 1000})s")
            }
        }
    }

    override fun start() {

    }

    override fun destroy() {

    }

    fun checkNetwork(): Observable<Boolean> {
        return Observable.create<Boolean> {
            val check = NetWorkUtil.isAvailable(EAPApplication.instance)
            it.onNext(check)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun login(map: HashMap<String, String>) {
        checkNetwork()
                .subscribe {
                    if (it) {
                        var requestStr = Gson().toJson(map)
                        var requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), requestStr)
                        LoginApi.IMPL.login(requestBody)
                                .subscribeOn(Schedulers.io())
                                .doOnSubscribe {
                                    mView.showStart()
                                }
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    mView.showDone()
                                    if (it.result == 0) {
                                        L(it.data[0].ArgFullAddress)
                                        mView.showLoadingCompany()
                                        mView.jumpToWeb(it)
                                    } else {
                                        mView.hideLoadingCompany()
                                        mView.showToast("登陆失败")
                                        SPUtil.clear(EAPApplication.instance)
                                    }
                                }
                    } else {
                        mView.showToast("暂无网络连接")
                    }
                }
    }

    override fun requestVerifyCode(mobile: String) {
        checkNetwork()
                .subscribe {
                    if (it) {
                        LoginApi.IMPL.getVerifyCode(mobile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    if (it.result == 0) {
                                        countDown.start()
                                    } else {
                                        mView.showToast(it.errmsg)
                                    }
                                }
                    } else {
                        mView.showToast("暂无网络连接")
                    }
                }


    }


}