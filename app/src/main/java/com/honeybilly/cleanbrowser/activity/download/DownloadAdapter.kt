package com.honeybilly.cleanbrowser.activity.download

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.honeybilly.cleanbrowser.R
import com.honeybilly.cleanbrowser.data.Download

/**
 * Created by liqi on 15:34.
 *
 *
 */
class DownloadAdapter : RecyclerView.Adapter<DownloadAdapter.DownloadHolder>() {

    private var downloadRecords: List<Download>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadHolder {
        return DownloadHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_download,parent,false))
    }

    override fun getItemCount(): Int {
        return if (downloadRecords == null) {
            0
        } else {
            downloadRecords!!.size
        }
    }

    override fun onBindViewHolder(holder: DownloadHolder, position: Int) {

    }


    inner class DownloadHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}