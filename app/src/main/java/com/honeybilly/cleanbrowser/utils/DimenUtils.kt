package com.honeybilly.cleanbrowser.utils

import android.content.Context

/**
 * Created by liqi on 11:25.
 *
 *
 */
class DimenUtils {

    companion object {
        fun dp2px(context: Context,dp:Int):Int{
            val metrics = context.resources.displayMetrics
            return (dp*metrics.density).toInt()
        }
    }
}