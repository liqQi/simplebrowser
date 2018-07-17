package com.honeybilly.cleanbrowser

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.honeybilly.cleanbrowser.eventbus.ProgressShowHideEvent
import org.greenrobot.eventbus.EventBus

/**
 * Created by liqi on 17:08.
 *
 *
 */
class MyWebViewClient : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        EventBus.getDefault().post(ProgressShowHideEvent(true))
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url
        val urlString = url.toString()
        if (urlString.startsWith("http")) {
            return false
        } else {
            val context = view?.context
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            val activity = context?.packageManager?.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (activity != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
                return true
            }
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        handler?.proceed()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        EventBus.getDefault().post(ProgressShowHideEvent(false))
    }
}