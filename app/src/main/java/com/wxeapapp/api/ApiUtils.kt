package com.wxeapapp.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-20 下午10:21
 * Copyright (c) 2017 nickming All right reserved.
 */
object ApiUtils {
    lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    val loginApi: LoginApi by lazy {
        val okClientBuilder = OkHttpClient.Builder().apply {
            this.addInterceptor(HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY })
            this.addInterceptor(com.wxeapapp.utils.cookies.WriteCookieInterceptor(com.wxeapapp.api.ApiUtils.context))
            this.addInterceptor(com.wxeapapp.utils.cookies.SaveCookieInterceptor(com.wxeapapp.api.ApiUtils.context))
            this.connectTimeout(3000, java.util.concurrent.TimeUnit.SECONDS)
        }
        Retrofit.Builder()
                .baseUrl("https://cloud.wy800.com/")
                .client(okClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(LoginApi::class.java)
    }
}