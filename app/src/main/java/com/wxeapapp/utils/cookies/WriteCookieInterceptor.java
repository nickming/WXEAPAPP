package com.wxeapapp.utils.cookies;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wxeapapp.utils.java.SPUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * class description here
 *
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-22 下午10:40
 * Copyright (c) 2017 nickming All right reserved.
 */

public class WriteCookieInterceptor implements Interceptor {

    private Context mContext;

    public WriteCookieInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String sid = (String) SPUtil.get(mContext, SPUtil.NET_SessionId, "");
        String token = (String) SPUtil.get(mContext, SPUtil.AppCloudToken, "");
        if (!TextUtils.isEmpty(sid)) {
//            Log.i("handle write cookie", sid);
//            builder.addHeader("Cookie", sid);
        }
        if (!TextUtils.isEmpty(token)) {
            Log.i("handle write cookie", token);
            builder.addHeader("Cookie", token);
        }
        return chain.proceed(builder.build());
    }
}
