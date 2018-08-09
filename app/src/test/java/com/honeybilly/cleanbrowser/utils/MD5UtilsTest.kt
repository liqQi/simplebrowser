package com.honeybilly.cleanbrowser.utils

import org.junit.Test

/**
 * Created by liqi on 14:44.
 */
class MD5UtilsTest{
    @Test
    fun testMd5(){
        val md5 = MD5Utils.md5("1")
        assert(md5 == "c4ca4238a0b923820dcc509a6f75849b")
    }
}