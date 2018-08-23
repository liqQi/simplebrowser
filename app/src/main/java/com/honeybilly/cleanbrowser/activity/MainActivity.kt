package com.honeybilly.cleanbrowser.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.honeybilly.cleanbrowser.R
import com.honeybilly.cleanbrowser.data.Menu
import com.honeybilly.cleanbrowser.eventbus.ProgressEvent
import com.honeybilly.cleanbrowser.eventbus.ProgressShowHideEvent
import com.honeybilly.cleanbrowser.eventbus.WebTitleChangeEvent
import com.honeybilly.cleanbrowser.utils.DimenUtils
import com.honeybilly.cleanbrowser.view.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

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
        more.setOnClickListener { showMoreMenu() }
        clickLayer.setOnClickListener{ showInputUrlDialog()}
    }

    private fun showInputUrlDialog() {
        InputUrlDialogFragment.newInstance().show(fragmentManager,TAG_INPUT_URL)
    }

    @SuppressLint("InflateParams")
    private fun showMoreMenu() {
        val popupWindow = PopupWindow(DimenUtils.dp2px(this, 120), ViewGroup.LayoutParams.WRAP_CONTENT)
        val child = LayoutInflater.from(this).inflate(R.layout.more_popup_window, null)
        val list = child.findViewById<RecyclerView>(R.id.list)
        list.addItemDecoration(DividerItemDecoration(getColor(R.color.divider_gray), 1))
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val moreMenuAdapter = MoreMenuAdapter()
        moreMenuAdapter.setOnItemClickListener(object : MoreMenuAdapter.OnItemClickListener {
            override fun onItemClick(menu: Menu) {
                val iconId = menu.iconId
                when (iconId) {
                    R.drawable.ic_home_black_24dp -> findCurrentWebFragment()?.initHomePage()
                    R.drawable.ic_close_black_24dp -> finish()
                    R.drawable.ic_star_black_24dp -> addBookMark()
                    R.drawable.ic_settings_black_24dp -> goSetting()
                }
                popupWindow.dismiss()
            }
        })
        list.adapter = moreMenuAdapter
        moreMenuAdapter.setMenus(prepareMenus())
        popupWindow.contentView = child
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupWindow.elevation = 4.0f
        popupWindow.showAsDropDown(more)
    }

    private fun goSetting() {
        startActivity(Intent(this,SettingsActivity::class.java))
    }

    private fun addBookMark() {
        val webFragment = findCurrentWebFragment()
        if(webFragment is WebViewFragment){
            webFragment.addBookMark()
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

    private fun prepareMenus(): ArrayList<Menu> {
        val menus = ArrayList<Menu>()
        menus.add(Menu("回到主页", R.drawable.ic_home_black_24dp))
        menus.add(Menu("设置", R.drawable.ic_settings_black_24dp))
        menus.add(Menu("添加书签", R.drawable.ic_star_black_24dp))
        menus.add(Menu("退出", R.drawable.ic_close_black_24dp))
        return menus
    }

    companion object {
        private const val TAG_PREFIX = "webfragment"
        private const val TAG_INPUT_URL = "input_url"
    }

}
