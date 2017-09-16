package com.wxeapapp.api

import com.wxeapapp.api.request.LoginResponse
import com.wxeapapp.model.SignOutItem
import com.wxeapapp.model.VerifyCodeItem
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-20 上午9:16
 * Copyright (c) 2017 nickming All right reserved.
 */
interface LoginApi {

    companion object {
        val IMPL: LoginApi = ApiUtils.loginApi
    }


    @GET("wxeap/oapi/Login/VerifyCode")
    fun getVerifyCode(@Query("mobile") phone: String): Observable<VerifyCodeItem>


    @Headers("Content-type:application/json;charset=UTF-8")
    @POST("wxeap/oapi/Login/Verify")
    fun login(@Body requestBody: RequestBody): Observable<LoginResponse>


    @GET("wxeap/oapi/Login/Out")
    fun signOut(): Observable<SignOutItem>


}