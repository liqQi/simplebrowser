package com.honeybilly.cleanbrowser.activity

import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.honeybilly.cleanbrowser.App
import com.honeybilly.cleanbrowser.R
import com.honeybilly.cleanbrowser.data.WebHistoryDao
import com.honeybilly.cleanbrowser.view.DividerItemDecoration
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.dialog_fragment_input_url.*
import java.util.concurrent.TimeUnit

/**
 * Created by liqi on 15:35.
 */
class InputUrlDialogFragment : DialogFragment() {
    private lateinit var historyAdapter: HistoryAdapter

    companion object {
        fun newInstance(): InputUrlDialogFragment {
            return InputUrlDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_fragment_input_url, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyList.layoutManager = LinearLayoutManager(context)
        historyList.addItemDecoration(DividerItemDecoration(context.getColor(R.color.divider_gray), 1))
        historyAdapter = HistoryAdapter()
        historyList.adapter = historyAdapter
        RxTextView.textChanges(editText)
                .skipInitialValue()
                .throttleLast(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t -> searchHistory(t) }, { t -> t.printStackTrace() })

    }

    private fun searchHistory(t: CharSequence?) {
        val url = t.toString()
        val list = App.instance.getSession().webHistoryDao.queryBuilder()
                .where(WebHistoryDao.Properties.Url.like("%$url%"))
                .orderDesc(WebHistoryDao.Properties.DateTime)
                .limit(10)
                .build()
                .list()
        historyAdapter.setWebHistories(list)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseDialogFragment)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                val attributes = window.attributes
                attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
                attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
                window.attributes = attributes
            }
        }
    }
}
