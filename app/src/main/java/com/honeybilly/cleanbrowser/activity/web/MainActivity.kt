package com.honeybilly.cleanbrowser.activity.web


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import com.honeybilly.cleanbrowser.App
import com.honeybilly.cleanbrowser.R
import com.honeybilly.cleanbrowser.activity.BaseActivity
import com.honeybilly.cleanbrowser.activity.setting.SettingsActivity
import com.honeybilly.cleanbrowser.data.BookMark
import com.honeybilly.cleanbrowser.data.FaviconFileDao
import com.honeybilly.cleanbrowser.data.Menu
import com.honeybilly.cleanbrowser.eventbus.NewUrlEvent
import com.honeybilly.cleanbrowser.eventbus.WebTitleChangeEvent
import com.honeybilly.cleanbrowser.utils.DimenUtils
import com.honeybilly.cleanbrowser.utils.StringUtils
import com.honeybilly.cleanbrowser.view.DividerItemDecoration
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    private var index = 0

    private var currentFocusFragmentTAG: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_main)
        val transaction = fragmentManager.beginTransaction()
        val tag = generateFragmentTAG()
        currentFocusFragmentTAG = tag
        transaction.add(R.id.container, WebViewFragment.newInstance(), tag)
        transaction.addToBackStack(tag)
        transaction.commit()
        back.setOnClickListener { onBackPressed() }
        more.setOnClickListener { showMoreMenu() }
        clickLayer.setOnClickListener { showInputUrlDialog() }
    }

    private fun showInputUrlDialog() {
        InputUrlDialogFragment.newInstance().show(fragmentManager, TAG_INPUT_URL)
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
                    R.drawable.ic_home_black_24dp -> popTopFragment()
                    R.drawable.ic_close_black_24dp -> finish()
                    R.drawable.ic_star_black_24dp -> addBookMark()
                    R.drawable.ic_settings_black_24dp -> goSetting()
                    R.drawable.ic_stars_black_24dp -> popupMyFavorite()
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

    private fun popupMyFavorite() {
        showProgress()
        Observable.create<List<BookMark>> { source ->
            val loadAll = App.instance.getSession().bookMarkDao.loadAll()
            loadAll.map { bookMark ->
                val domain = StringUtils.getDomain(bookMark.url)
                val list = App.instance.getSession().faviconFileDao.queryBuilder().where(FaviconFileDao.Properties.Domain.eq(domain)).build().list()
                if (!list.isEmpty()) {
                    val faviconFile = list[0]
                    bookMark.faviconFile = faviconFile
                }
            }
            source.onNext(loadAll)
            source.onComplete()
        }.delay(500,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bookMarks ->
                    hideProgress()
                    showBookMarks(bookMarks)
                }, { e ->
                    hideProgress()
                    e.printStackTrace()
                })

    }

    private fun showBookMarks(bookMarks: List<BookMark>?) {
        val popupMenu = PopupMenu(this, more)
        bookMarks?.forEach { bookMark ->
            popupMenu.menu.add(android.view.Menu.NONE, bookMark._id.toInt(), android.view.Menu.NONE, bookMark.title)
        }
        popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            val itemId = item?.itemId
            val find = bookMarks?.find { bookMark -> itemId == bookMark._id.toInt() }
            onNewUrlEvent(NewUrlEvent(find?.url))
            popupMenu.dismiss()
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    private fun popTopFragment() {
        val topFragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name
        val plus = TAG_PREFIX.plus(0)
        if (topFragmentTag != plus) {
            fragmentManager.popBackStackImmediate()
            popTopFragment()
        } else {
            val fragment = fragmentManager.findFragmentByTag(topFragmentTag)
            if (fragment is WebViewFragment) {
                setUpTitleAndUrl(fragment)
            }
        }
    }

    private fun setUpTitleAndUrl(fragment: WebViewFragment?) {
        findViewById<TextView>(R.id.title).text = fragment?.myWebChromeClient?.title
        findViewById<TextView>(R.id.subtitle).text = fragment?.myWebChromeClient?.url
    }

    private fun goSetting() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun addBookMark() {
        getTopStackFragment()?.addBookMark()
    }

    @Suppress("unused")
    @Subscribe
    fun onTitleChange(titleEvent: WebTitleChangeEvent) {
        findViewById<TextView>(R.id.title).text = titleEvent.title
        findViewById<TextView>(R.id.subtitle).text = titleEvent.url
    }

//    @Subscribe
//    fun onProgressShowHide(showHide: ProgressShowHideEvent) {
//        progress.progress = 0
//    }

    @Subscribe
    fun onNewUrlEvent(newUrlEvent: NewUrlEvent) {
        val webViewFragment = WebViewFragment.newInstance(newUrlEvent.url)
        val beginTransaction = fragmentManager.beginTransaction()
        beginTransaction.setCustomAnimations(R.animator.fragment_slide_in,R.animator.fragment_slide_out,R.animator.fragment_slide_in,R.animator.fragment_slide_out)
        val tag = generateFragmentTAG()
        beginTransaction.add(R.id.container, webViewFragment, tag)
        beginTransaction.addToBackStack(tag)
        beginTransaction.commitAllowingStateLoss()
    }

//    @Subscribe
//    fun onProgressChange(progressValue: ProgressEvent) {
//        progress.progress = progressValue.progress
//    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragmentManager.backStackEntryCount != 0) {
            setUpTitleAndUrl(getTopStackFragment())
        } else {
            finish()
        }
    }

    private fun getTopStackFragment(): WebViewFragment? {
        val name = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name
        return fragmentManager.findFragmentByTag(name) as WebViewFragment?
    }

    private fun generateFragmentTAG(): String {
        val temp = index
        index++
        return TAG_PREFIX + temp
    }

    private fun prepareMenus(): ArrayList<Menu> {
        val menus = ArrayList<Menu>()
        menus.add(Menu("回到主页", R.drawable.ic_home_black_24dp))
        menus.add(Menu("我的书签", R.drawable.ic_stars_black_24dp))
        menus.add(Menu("设置", R.drawable.ic_settings_black_24dp))
        menus.add(Menu("添加书签", R.drawable.ic_star_black_24dp))
        menus.add(Menu("退出", R.drawable.ic_close_black_24dp))
        return menus
    }

    fun startNewPage(text: String?) {
        if (text != null) {
            var url = text
            if (!url.startsWith(WebViewFragment.HTTP) && !url.startsWith(WebViewFragment.HTTPS)) {
                url = WebViewFragment.HTTP + url
            }
            onNewUrlEvent(NewUrlEvent(url))
        }
    }

    override fun shouldInterceptTitleChange(): Boolean {
        return false
    }

    companion object {
        private const val TAG_PREFIX = "webfragment"
        private const val TAG_INPUT_URL = "input_url"
    }

}
