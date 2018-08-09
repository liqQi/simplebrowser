package com.honeybilly.cleanbrowser

import android.app.Application
import com.honeybilly.cleanbrowser.data.DaoMaster
import com.honeybilly.cleanbrowser.data.DaoSession
import com.honeybilly.cleanbrowser.data.WebViewOpenHelper

/**
 * Created by liqi on 11:36.
 *
 *
 */
class App : Application() {
    private lateinit var daoSession: DaoSession
    override fun onCreate() {
        super.onCreate()
        instance = this

        val helper = WebViewOpenHelper(this, DB_NAME)
        val db = helper.writableDb
        daoSession = DaoMaster(db).newSession()
    }

    fun getSession(): DaoSession = daoSession

    companion object {
        lateinit var instance: App
            private set
        private const val DB_NAME = "web"
    }
}