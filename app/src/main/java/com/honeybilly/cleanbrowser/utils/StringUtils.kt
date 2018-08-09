package com.honeybilly.cleanbrowser.utils

import java.net.URI


/**
 * Created by liqi on 14:58.
 */
class StringUtils{


    companion object {
        fun getDomain(url:String):String?{
            val uri = URI(url)
            val hostname = uri.host
            return if (hostname != null) {
                if (hostname.startsWith("www.")) hostname.substring(4) else hostname
            } else hostname
        }
    }
}
