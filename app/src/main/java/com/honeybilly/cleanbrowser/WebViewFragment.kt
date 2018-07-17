package com.honeybilly.cleanbrowser

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.fragment_webview.*

/**
 * Created by liqi on 17:18.
 *
 *
 */

private const val HOME_URL: String = "https://www.baidu.com/?tn=simple#"
private const val USER_AGRNT: String = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"

class WebViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webview,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.webChromeClient = MyWebChromeClient()
        webView.webViewClient = MyWebViewClient()
        val settings = webView.settings
        settings.userAgentString = USER_AGRNT
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.allowFileAccessFromFileURLs = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        settings.allowUniversalAccessFromFileURLs = true
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = false
        settings.blockNetworkImage = false
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.loadUrl(HOME_URL)
    }

    fun canGoBack() : Boolean{
        if(webView.canGoBack()){
            webView.goBack()
            return true
        }
        return false
    }

    fun initHomePage() {
        webView.loadUrl(HOME_URL)
    }

    companion object {
        fun newInstance():WebViewFragment{
            return WebViewFragment()
        }

    }
}