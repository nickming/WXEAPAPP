package com.wxeapapp.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * class description here
 * @author nickming
 * @version 1.0.0
 * @since 2017-08-22 下午1:15
 * Copyright (c) 2017 nickming All right reserved.
 */
object LoginUtil {

    /**
     * 校验是否为手机号码
     */
    fun isPhoneNum(str: String): Boolean {
        var pattern: Pattern = Pattern.compile("1[0-9]{10}")
        var matcher: Matcher = pattern.matcher(str)
        return matcher.matches()
    }
}