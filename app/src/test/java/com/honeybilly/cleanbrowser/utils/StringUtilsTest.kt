package com.honeybilly.cleanbrowser.utils

import org.junit.Test

/**
 * Created by liqi on 15:03.
 */
class StringUtilsTest {
    @Test
    fun testGetDomain() {
        val url = "http://www.baidu.com"
        assert("baidu.com" == StringUtils.getDomain(url))
    }
}