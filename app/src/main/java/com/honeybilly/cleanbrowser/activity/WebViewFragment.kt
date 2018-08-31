package com.honeybilly.cleanbrowser.activity

import android.annotation.SuppressLint
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.Toast
import com.honeybilly.cleanbrowser.App
import com.honeybilly.cleanbrowser.R
import com.honeybilly.cleanbrowser.data.BookMark
import com.honeybilly.cleanbrowser.utils.isNetworkAvailable
import com.honeybilly.cleanbrowser.view.MyWebChromeClient
import com.honeybilly.cleanbrowser.view.MyWebViewClient
import kotlinx.android.synthetic.main.fragment_webview.*

class WebViewFragment : Fragment() {

    lateinit var myWebChromeClient: MyWebChromeClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        myWebChromeClient = MyWebChromeClient()
        myWebChromeClient.setOnProgressListener(object : MyWebChromeClient.OnProgressListener {
            override fun onProgressChange(newProgress: Int) {
                progress.progress = newProgress
            }
        })
        webView.webChromeClient = myWebChromeClient
        val myWebViewClient = MyWebViewClient()
        myWebViewClient.setListener(object :MyWebViewClient.OnPageChangeListener{
            override fun onPageStart() {
                progress.visibility = View.VISIBLE
            }

            override fun onPageFinish() {
                progress.visibility = View.INVISIBLE
            }
        })
        webView.webViewClient = myWebViewClient
        val settings = webView.settings
        settings.userAgentString = USER_AGENT_MOBILE
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.allowFileAccessFromFileURLs = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.setAppCacheEnabled(true)
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        if (isNetworkAvailable(activity)) {
            settings.cacheMode = WebSettings.LOAD_DEFAULT
        } else {
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = false
        settings.blockNetworkImage = false
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        val get = arguments?.getString(URL)
        if (get != null && !get.isEmpty()) {
            webView.loadUrl(get)
        } else {
            webView.loadUrl(HOME_URL)
        }
    }

    fun addBookMark() {
        val bookMark = BookMark()
        bookMark.url = webView.url
        bookMark.title = webView.title
        bookMark.timeStamp = System.currentTimeMillis()
        App.instance.getSession().bookMarkDao.insertOrReplace(bookMark)
        Toast.makeText(context, R.string.add_book_mark_success, Toast.LENGTH_SHORT).show()
    }


    fun setUrl(text: String?) {
        if (text != null) {
            var url = text
            if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
                url = HTTP + url
            }
            webView.loadUrl(url)
        }
    }

    companion object {
        fun newInstance(): WebViewFragment {
            return WebViewFragment()
        }

        fun newInstance(url: String?): WebViewFragment {
            if (url == null) {
                return newInstance()
            }
            val webViewFragment = WebViewFragment()
            val bundle = Bundle()
            bundle.putString(URL, url)
            webViewFragment.arguments = bundle
            return webViewFragment
        }

        /**
         * Created by liqi on 17:18.
         *
         *
         */

        const val HOME_URL: String = "https://www.baidu.com/?tn=simple#"
        const val USER_AGENT_MOBILE: String = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36"
        const val USER_AGENT_LAPTOP: String = "Mozilla/5.0 (Windows NT 5.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36"
        const val HTTP = "http://"
        const val HTTPS = "https://"

        const val URL = "url"
    }
}