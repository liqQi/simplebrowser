package com.honeybilly.cleanbrowser.utils

import java.security.MessageDigest
import java.util.*

/**
 * Created by liqi on 14:36.
 */
class MD5Utils {
    companion object {
        fun md5(str: String): String {
            val messageDigest = MessageDigest.getInstance("md5")
            val digest = messageDigest.digest(str.toByteArray())
            messageDigest.digest()
            val sb = StringBuilder()
            for (byte in digest) {
                val i :Int = byte.toInt() and 0xff//获取低八位有效值
                var hexString = Integer.toHexString(i)//将整数转化为16进制
                if (hexString.length < 2) {
                    hexString = "0$hexString"//如果是一位的话，补0
                }
                sb.append(hexString)
            }
            return sb.toString().toUpperCase()
        }

        fun generateID():String{
            val randomUUID = UUID.randomUUID()
            println(randomUUID)
            return md5(randomUUID.toString())
        }
    }
}
