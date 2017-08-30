package com.wxeapapp.ui.web

import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.wxeapapp.model.PayLoad
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-25 上午12:44
 * Copyright (c) 2017 nickming All right reserved.
 */
class AndroidInterface(val mDelegate: IWebActionDelegate) {

    companion object {
        val onShouldPush = "onShouldPush"
        val onTitleUpdate = "onTitleUpdate"
        val onGoBack = "onGoBack"
        val onShowImagePicker = "onShowImagePicker"
        val onSwitchSystem = "onSwitchSystem"
        val onLogout = "onLogout"
    }

    @JavascriptInterface
    fun postMessage(message: String): Unit {
        val payLoadItem: PayLoad = Gson().fromJson(message, PayLoad::class.java)
        Log.i("postMessage:", message)
        when (payLoadItem.type) {
            onShouldPush -> {
                getObservableOnMainThread(payLoadItem)
                        .subscribe {
                            mDelegate.onShouldPush(it)
                        }
            }
            onTitleUpdate -> {
                getObservableOnMainThread(payLoadItem)
                        .subscribe {
                            mDelegate.onTitleUpdate(it)
                        }
            }
            onGoBack -> {
                getObservableOnMainThread(payLoadItem)
                        .subscribe {
                            mDelegate.onGoBack(it)
                        }
            }
            onShowImagePicker -> {
                getObservableOnMainThread(payLoadItem)
                        .subscribe {
                            mDelegate.onShowImagePicker(it)
                        }
            }
            onSwitchSystem -> {
                getObservableOnMainThread(payLoadItem)
                        .subscribe {
                            mDelegate.onSwitchSystem(it)
                        }
            }
            onLogout -> {
                getObservableOnMainThread(payLoadItem)
                        .subscribe {
                            mDelegate.onLogout(it)
                        }
            }
        }
    }

    fun getObservableOnMainThread(payLoad: PayLoad): Observable<PayLoad> {
        return Observable.create<PayLoad> {
            it.onNext(payLoad)
            it.onComplete()
        }
                .observeOn(AndroidSchedulers.mainThread())
    }

}