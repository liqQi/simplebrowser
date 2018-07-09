package com.honeybilly.cleanbrowser

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.honeybilly.cleanbrowser.eventbus.WebTitleChangeEvent
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

    private var fragmentIndex: Int = 0
    private val fragmentTagPrefix: String = "WEB_FRAGMENT"
    private var currentFocusFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(R.id.container, WebFragment(), generateFragmentTAG())
        beginTransaction.commit()
    }


    @Subscribe
    fun onTitleChange(title: WebTitleChangeEvent) {
        supportActionBar?.title = title.title
        supportActionBar?.subtitle = title.url
    }

    fun generateFragmentTAG(): String {
        val result = fragmentTagPrefix + fragmentIndex
        fragmentIndex++
        currentFocusFragmentTag = result
        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(currentFocusFragmentTag)
        if(fragment is WebFragment){
            val result = fragment.onBackPressed()
            if(result){
                return
            }
        }
        super.onBackPressed()
    }
}
