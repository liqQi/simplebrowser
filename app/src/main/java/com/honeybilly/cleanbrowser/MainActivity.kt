package com.honeybilly.cleanbrowser


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.widget.PopupMenu
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

    private var currentFocusFragmentTAG: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_main)
        val transaction = supportFragmentManager.beginTransaction()
        val tag = generateFragmentTAG()
        currentFocusFragmentTAG = tag
        transaction.add(R.id.container, WebViewFragment.newInstance(), tag)
        transaction.commit()
        back.setOnClickListener { onBackPressed() }
        more.setOnClickListener { v ->
            run {
                val popupMenu = PopupMenu(v.context, v, Gravity.BOTTOM)
                popupMenu.inflate(R.menu.more)
                popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
                    val itemId = item?.itemId
                    when (itemId) {
                        R.id.exit -> finish()
                        R.id.backToHome ->findCurrentWebFragment()?.initHomePage()
                    }
                    return@setOnMenuItemClickListener true
                }
                popupMenu.show()
            }
        }
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

    private fun findCurrentWebFragment(): WebViewFragment? {
        val fragment = supportFragmentManager.findFragmentByTag(currentFocusFragmentTAG)
        return fragment as? WebViewFragment

    }

    override fun onBackPressed() {
        val findCurrentWebFragment = findCurrentWebFragment()
        if (findCurrentWebFragment != null) {
            val canGoBack = findCurrentWebFragment.canGoBack()
            if (canGoBack) {
                return
            }
        }
        super.onBackPressed()
    }

    private fun generateFragmentTAG(): String {
        val temp = index
        index++
        return TAG_PREFIX + temp
    }
}
