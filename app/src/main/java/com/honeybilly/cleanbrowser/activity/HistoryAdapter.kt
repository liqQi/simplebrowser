package com.honeybilly.cleanbrowser.activity

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.honeybilly.cleanbrowser.App
import com.honeybilly.cleanbrowser.R
import com.honeybilly.cleanbrowser.activity.HistoryAdapter.HistoryViewHolder
import com.honeybilly.cleanbrowser.data.FaviconFileDao
import com.honeybilly.cleanbrowser.data.WebHistory
import com.honeybilly.cleanbrowser.utils.OnItemClickListener
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*

/**
 * Created by liqi on 17:30.
 */
class HistoryAdapter : Adapter<HistoryViewHolder>(), View.OnClickListener {
    override fun onClick(v: View) {
        if(v.tag is WebHistory){
            onItemClickListener?.onItemClick(v.tag as WebHistory)
        }
    }

    var onItemClickListener: OnItemClickListener<WebHistory>? = null

    private var webHistories: List<WebHistory> = ArrayList()

    fun setOnItemClickListener(action : (WebHistory)->Unit){
        onItemClickListener = object :OnItemClickListener<WebHistory>{
            override fun onItemClick(t: WebHistory) {
                action(t)
            }
        }
    }

    fun setWebHistories(webHistories: List<WebHistory>?) {
        if (webHistories == null) {
            this.webHistories = Collections.emptyList()
        } else {
            this.webHistories = webHistories
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_web_history, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bindWebHistory(webHistories[position])
        holder.itemView.tag = webHistories[position]
        holder.itemView.setOnClickListener(this)
    }

    override fun getItemCount(): Int {
        return webHistories.size
    }

    class HistoryViewHolder(itemView: View) : ViewHolder(itemView) {
        private val faviconFileIv = itemView.findViewById<ImageView>(R.id.favicon)
        private val url = itemView.findViewById<TextView>(R.id.url)
        private val title = itemView.findViewById<TextView>(R.id.title)

        fun bindWebHistory(webHistory: WebHistory) {
            title.text = webHistory.title
            url.text = webHistory.url
            Observable.just(webHistory)
                    .flatMap {
                        val domain = webHistory.domain
                        val list = App.instance.getSession().faviconFileDao.queryBuilder().where(FaviconFileDao.Properties.Domain.like("%$domain%")).build().list()
                        if (list.isEmpty()) {
                            Observable.error(NoSuchFieldError("can not find this favicon"))
                        } else {
                            Observable.just(list[0])
                        }
                    }.flatMap { faviconFile ->
                        val filePath = faviconFile.filePath
                        Observable.just(filePath)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ filePath -> Picasso.get().load(File(filePath)).error(R.drawable.ic_broken_image_black_24dp).into(faviconFileIv) }, { e -> e.printStackTrace() })
        }
    }
}
