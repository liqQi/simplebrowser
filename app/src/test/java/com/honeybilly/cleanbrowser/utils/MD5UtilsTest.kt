package com.honeybilly.cleanbrowser.utils

import android.util.Log
import org.junit.Test

/**
 * Created by liqi on 14:44.
 */
class MD5UtilsTest {
    @Test
    fun testMd5() {
        val md5 = MD5Utils.md5("1")
        assert(md5 == "C4CA4238A0B923820DCC509A6F75849B")
    }

    @Test
    fun testGenerateId() {
        val generateID = MD5Utils.generateID()
        println("generateId = $generateID")
        assert(generateID.length == 32)
    }
}