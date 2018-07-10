package com.honeybilly.cleanbrowser

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.honeybilly.cleanbrowser.eventbus.WebTitleChangeEvent
import kotlinx.android.synthetic.main.fragment_web.*
import kotlinx.android.synthetic.main.fragment_web.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by liqi on 16:07.
 *
 *
 */

private const val HOME_URL: String = "https://www.baidu.com/?tn=simple#"

class WebFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = view.webView
        webView.webChromeClient = MyWebChromeClient()
        webView.webViewClient = MyWebViewClient()
        val settings = webView.settings
        settings.builtInZoomControls = true
        settings.allowFileAccessFromFileURLs = true
        settings.loadWithOverviewMode = true
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        settings.allowUniversalAccessFromFileURLs = true
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = false
        webView.loadUrl(HOME_URL)
        progress.max = 100
    }

    fun onBackPressed(): Boolean {
        val canGoBack = webView.canGoBack()
        if (canGoBack) {
            webView.goBack()
        }
        return canGoBack
    }

    inner class MyWebChromeClient : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            EventBus.getDefault().post(WebTitleChangeEvent(view?.title, view?.url))
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progress.progress = newProgress
        }

    }

    inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progress.visibility = View.VISIBLE
            progress.progress = 0
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url = request?.url
            val urlString = url.toString()
            if (urlString.startsWith("http")) {
                return false
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(urlString)
                val activity = context?.packageManager?.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                if (activity != null) {
                    startActivity(intent)
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
            progress.visibility = View.INVISIBLE
        }
    }

}

