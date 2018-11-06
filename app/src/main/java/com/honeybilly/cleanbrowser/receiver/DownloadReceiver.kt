package com.honeybilly.cleanbrowser.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils

import android.content.Context.DOWNLOAD_SERVICE

/**
 * Created by liqi on 15:04.
 */
class DownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                var type = downloadManager.getMimeTypeForDownloadedFile(downloadId)
                if (TextUtils.isEmpty(type)) {
                    type = "*/*"
                }
                val uri = downloadManager.getUriForDownloadedFile(downloadId)
                if (uri != null) {
                    val handlerIntent = Intent(Intent.ACTION_VIEW)
                    handlerIntent.setDataAndType(uri, type)
                    context.startActivity(handlerIntent)
                }
            }
        }
    }
}
