package com.honeybilly.cleanbrowser.activity.web

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.honeybilly.cleanbrowser.R
import com.honeybilly.cleanbrowser.data.Menu
import com.honeybilly.cleanbrowser.utils.DimenUtils

/**
 * Created by liqi on 14:56.
 *
 *
 */
class MoreMenuAdapter : RecyclerView.Adapter<MoreMenuAdapter.MoreMenuHolder>(), View.OnClickListener {


    interface OnItemClickListener{
        fun onItemClick(menu: Menu)
    }

    override fun onClick(v: View?) {
        val tag = v?.tag
        if(tag is Menu){
            if(onItemClickListener!=null){
                onItemClickListener!!.onItemClick(tag)
            }
        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    private var menus: ArrayList<Menu> = ArrayList()

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }

    fun setMenus(menus: ArrayList<Menu>?) {
        if (menus == null) {
            this.menus = ArrayList()
        } else {
            this.menus = menus
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreMenuHolder {
        val context = parent.context
        val tv = TextView(context)
        val params = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, DimenUtils.dp2px(context, 36))
        tv.layoutParams = params
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        tv.setTextColor(context.getColor(R.color.text_color_main))
        tv.setPadding( 0, 0, DimenUtils.dp2px(context, 6), 0)
        tv.gravity = Gravity.END or Gravity.CENTER_VERTICAL
        tv.compoundDrawablePadding = DimenUtils.dp2px(context, 4)
        return MoreMenuHolder(tv)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: MoreMenuHolder, position: Int) {
        if (holder.itemView is TextView) {
            val tv: TextView = holder.itemView
            val context = tv.context
            val menu = menus[position]
            @Suppress("DEPRECATION")
            val drawable = context.resources.getDrawable(menu.iconId)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            tv.setCompoundDrawables(null, null,drawable, null)
            tv.text = menu.title
            tv.tag = menu
            tv.setOnClickListener(this)
        }
    }


    class MoreMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}