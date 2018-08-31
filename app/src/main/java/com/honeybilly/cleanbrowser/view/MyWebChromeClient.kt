package com.honeybilly.cleanbrowser.view

import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.honeybilly.cleanbrowser.App
import com.honeybilly.cleanbrowser.activity.WebViewFragment
import com.honeybilly.cleanbrowser.data.FaviconFile
import com.honeybilly.cleanbrowser.data.FaviconFileDao
import com.honeybilly.cleanbrowser.data.WebHistory
import com.honeybilly.cleanbrowser.data.WebHistoryDao
import com.honeybilly.cleanbrowser.eventbus.WebTitleChangeEvent
import com.honeybilly.cleanbrowser.utils.MD5Utils
import com.honeybilly.cleanbrowser.utils.StringUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileOutputStream

/**
 * Created by liqi on 17:07.
 *
 *
 */

class MyWebChromeClient : WebChromeClient() {
    private val webHistoryDao: WebHistoryDao = App.instance.getSession().webHistoryDao
    private val faviconFileDao: FaviconFileDao = App.instance.getSession().faviconFileDao
    private var onProgressListener: OnProgressListener? = null

    var title:String? = null
    var url:String? = null

    fun setOnProgressListener(l : OnProgressListener){
        onProgressListener = l
    }

    override fun onReceivedTitle(view: WebView, title: String?) {
        super.onReceivedTitle(view, title)
        EventBus.getDefault().post(WebTitleChangeEvent(view.title, view.url))
        this.title = title
        url = view.url
        if(view.url == WebViewFragment.HOME_URL){
            return
        }
        val webHistory = WebHistory()
        webHistory.dateTime = System.currentTimeMillis()
        webHistory.title = title
        webHistory.url = view.url
        webHistory.domain = StringUtils.getDomain(view.url)
        webHistoryDao.insertOrReplace(webHistory)
    }

    override fun onReceivedIcon(view: WebView, icon: Bitmap) {
        super.onReceivedIcon(view, icon)
        val url = view.url
        Observable.create(ObservableOnSubscribe<String> { e ->
            e.onNext(url)
        })
                .subscribeOn(Schedulers.io())
                .subscribe({ webUrl -> saveIcon(webUrl, icon) }, { error -> error.printStackTrace() })
    }

    private fun saveIcon(url: String, icon: Bitmap) {
        val fileDir = getIconDir()
        val domain: String = StringUtils.getDomain(url) ?: return
        val queryRaw = faviconFileDao.queryRaw("where DOMAIN=?", domain)
        if (queryRaw != null && queryRaw.size == 1) return
        val fileName = MD5Utils.md5(domain)
        val file: File
        try {
            file = File(fileDir, fileName)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val fos = FileOutputStream(file)
            icon.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        }catch (e:Exception){
            e.printStackTrace()
            return
        }
        val faviconFile = FaviconFile()
        faviconFile.domain = domain
        faviconFile.filePath = file.absolutePath
        faviconFile.time = System.currentTimeMillis()
        faviconFileDao.insert(faviconFile)
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
//        EventBus.getDefault().post(ProgressEvent(newProgress))
        onProgressListener?.onProgressChange(newProgress)
    }

    private fun getIconDir(): String {
        return File(App.instance.filesDir.absolutePath, "icon").absolutePath
    }

    interface OnProgressListener{
        fun onProgressChange(newProgress: Int)
    }

}