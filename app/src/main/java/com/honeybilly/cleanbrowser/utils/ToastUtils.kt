package com.honeybilly.cleanbrowser.utils

import android.widget.Toast
import com.honeybilly.cleanbrowser.App

/**
 * Created by liqi on 11:44.
 *
 *
 */
fun showToast(message:String?){
    Toast.makeText(App.instance,message,Toast.LENGTH_SHORT).show()
}