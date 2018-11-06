package com.honeybilly.cleanbrowser.view

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.view.LayoutInflater
import android.webkit.*
import android.widget.EditText
import android.widget.Toast
import com.honeybilly.cleanbrowser.R
import com.honeybilly.cleanbrowser.eventbus.NewUrlEvent
import com.honeybilly.cleanbrowser.eventbus.WebTitleChangeEvent
import com.honeybilly.cleanbrowser.utils.showToast
import org.greenrobot.eventbus.EventBus

/**
 * Created by liqi on 17:08.
 *
 *
 */
class MyWebViewClient : WebViewClient() {

    private var listener: OnPageChangeListener? = null

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
//        EventBus.getDefault().post(ProgressShowHideEvent(true))
        listener?.onPageStart()
        EventBus.getDefault().post(WebTitleChangeEvent(view?.title, url))
    }

    fun setListener(onPageChangeListener: OnPageChangeListener) {
        this.listener = onPageChangeListener
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url
        val urlString = url.toString()
        if (urlString.startsWith("http")) {
            EventBus.getDefault().post(NewUrlEvent(urlString))
            return true
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
//        EventBus.getDefault().post(ProgressShowHideEvent(false))
        listener?.onPageFinish()
    }

    override fun onReceivedHttpAuthRequest(view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) {
        val context = view?.context
        val dialog = AlertDialog.Builder(context).create()
        dialog.setTitle(R.string.validate)
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_input_http_auth, null)
        val userName = contentView.findViewById<EditText>(R.id.userName)
        val passWord = contentView.findViewById<EditText>(R.id.password)
        dialog.setContentView(contentView)
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context?.getString(R.string.confirm)) { dialogInterface, _ ->
            val userNameStr = userName.text.toString()
            val passwordStr = passWord.text.toString()
            if (userNameStr.isEmpty() || passwordStr.isEmpty()) {
                showToast(context?.getString(R.string.please_input_user_name_and_password))
            } else {
                handler?.proceed(userNameStr, passwordStr)
                dialogInterface.dismiss()
            }
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context?.getText(R.string.cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
            handler?.cancel()
        }
        dialog.show()
    }

    interface OnPageChangeListener {
        fun onPageStart()
        fun onPageFinish()
    }
}