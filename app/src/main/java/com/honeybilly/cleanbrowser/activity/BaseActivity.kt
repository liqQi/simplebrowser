@file:Suppress("DEPRECATION")

package com.honeybilly.cleanbrowser.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.honeybilly.cleanbrowser.R


/**
 * Created by liqi on 14:36.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("加载中...")
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
    }

    fun showProgress() {
        if (!progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    fun hideProgress() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (shouldInterceptTitleChange()) {
            val textView = findViewById<TextView>(R.id.title)
            val activityInfo = packageManager.getActivityInfo(componentName, 0)
            textView?.text = getString(activityInfo.labelRes)
        }
    }

    open fun shouldInterceptTitleChange(): Boolean {
        return true
    }
}
