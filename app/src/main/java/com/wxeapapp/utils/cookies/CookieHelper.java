package com.wxeapapp.utils.cookies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.List;

/**
 * class description here
 *
 * @author nickming
 * @version 1.0.0
 * @since 2017-09-02 下午11:47
 * Copyright (c) 2017 nickming All right reserved.
 */

public class CookieHelper {

    public static void setCookie(boolean index, String url, List<String> cookies, Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        if (index){
            cookieManager.removeAllCookie();
        }
        cookieManager.setAcceptCookie(true);
        for (String cookie : cookies) {
            cookieManager.setCookie(url, cookie);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
            return;
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    CookieManager.getInstance().flush();
                }

            }
        });
    }
}
