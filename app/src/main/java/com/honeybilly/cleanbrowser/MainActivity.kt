package com.honeybilly.cleanbrowser

import android.os.Bundle


import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.honeybilly.cleanbrowser.eventbus.ProgressEvent
import com.honeybilly.cleanbrowser.eventbus.ProgressShowHideEvent
import com.honeybilly.cleanbrowser.eventbus.WebTitleChangeEvent
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

private const val TAG_PREFIX = "webfragment"

class MainActivity : AppCompatActivity() {

    private var index = 0

    private var currentFocusFragmentTAG:String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_main)
        val transaction = supportFragmentManager.beginTransaction()
        val tag = generateFragmentTAG()
        currentFocusFragmentTAG = tag
        transaction.add(R.id.container,WebViewFragment.newInstance(), tag)
        transaction.commit()
    }

    @Suppress("unused")
    @Subscribe
    fun onTitleChange(titleEvent: WebTitleChangeEvent) {
        findViewById<TextView>(R.id.title).text = titleEvent.title
        findViewById<TextView>(R.id.subtitle).text = titleEvent.url
    }

    @Subscribe
    fun onProgressShowHide(showHide: ProgressShowHideEvent) {
        progress.progress = 0
    }

    @Subscribe
    fun onProgressChange(progressValue: ProgressEvent) {
        progress.progress = progressValue.progress
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(currentFocusFragmentTAG)
        if(fragment is WebViewFragment){
            val canGoBack = fragment.canGoBack()
            if(canGoBack){
                return
            }
        }
        super.onBackPressed()
    }

    private fun generateFragmentTAG(): String {
        val temp = index
        index++
        return TAG_PREFIX+temp
    }
}
