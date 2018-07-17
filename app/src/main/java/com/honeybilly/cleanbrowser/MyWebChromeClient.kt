package com.honeybilly.cleanbrowser

import android.webkit.WebChromeClient
import android.webkit.WebView
import com.honeybilly.cleanbrowser.eventbus.ProgressEvent
import com.honeybilly.cleanbrowser.eventbus.WebTitleChangeEvent
import org.greenrobot.eventbus.EventBus

/**
 * Created by liqi on 17:07.
 *
 *
 */

class MyWebChromeClient : WebChromeClient() {
    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        EventBus.getDefault().post(WebTitleChangeEvent(view?.title, view?.url))
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        EventBus.getDefault().post(ProgressEvent(newProgress))
    }

}