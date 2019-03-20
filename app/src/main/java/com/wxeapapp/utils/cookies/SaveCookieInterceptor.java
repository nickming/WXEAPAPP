package com.wxeapapp.utils.cookies;

import android.content.Context;
import android.util.Log;

import com.wxeapapp.utils.java.SPUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * class description here
 *
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-22 下午9:59
 * Copyright (c) 2017 nickming All right reserved.
 */

public class SaveCookieInterceptor implements Interceptor {
    private Context context;

    public SaveCookieInterceptor(Context context) {
        super();
        this.context = context;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            for (String header : originalResponse.headers("Set-Cookie")) {
                String cookie = header.toString();
//                if (cookie.contains(SPUtil.NET_SessionId)) {
//                    Log.i("handle save_sid:", cookie);
//                    SPUtil.put(context, SPUtil.NET_SessionId, cookie);
//                }
                if (cookie.contains(SPUtil.AppCloudToken)) {
                    Log.i("handle save_token:", cookie);
                    SPUtil.put(context, SPUtil.AppCloudToken, cookie);
                }
            }
        }
        return originalResponse;
    }
}
