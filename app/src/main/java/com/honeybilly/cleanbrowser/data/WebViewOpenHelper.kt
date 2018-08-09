package com.honeybilly.cleanbrowser.data

import android.content.Context
import org.greenrobot.greendao.database.Database

/**
 * Created by liqi on 17:35.
 *
 *
 */
class WebViewOpenHelper(context: Context, name: String) : DaoMaster.OpenHelper(context, name) {
    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        super.onUpgrade(db, oldVersion, newVersion)
    }
}