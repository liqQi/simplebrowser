package com.honeybilly.cleanbrowser.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by liqi on 15:14.
 *
 *
 */
fun isNetworkAvailable(activity:Activity?):Boolean{
    val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = cm.activeNetworkInfo
    return activeNetworkInfo!=null && activeNetworkInfo.isConnected
}

fun isCurrentUseWifi(activity: Activity?):Boolean{
    val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = cm.activeNetworkInfo
    return activeNetworkInfo!=null && activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
}