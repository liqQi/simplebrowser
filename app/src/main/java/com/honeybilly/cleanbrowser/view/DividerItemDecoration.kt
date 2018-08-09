package com.honeybilly.cleanbrowser.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.annotation.ColorInt
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by liqi on 14:52.
 *
 *
 */


class DividerItemDecoration(@ColorInt val color: Int, val height: Int) : RecyclerView.ItemDecoration() {

    private var paint: Paint = Paint()

    init {
        paint.color = color
        paint.isAntiAlias = true
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val position = parent.getChildViewHolder(view).adapterPosition
        val itemCount = parent.adapter.itemCount
        if (position != itemCount - 1) {
            outRect.set(0, 0, 0, height)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val childCount = parent.childCount
        val left = parent.paddingLeft + 0.0f
        val right = parent.width - parent.paddingRight + 0.0f
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val top = view.bottom + 0.0f
            val bottom = view.bottom + height + 0.0f
            canvas.drawRect(left, top, right, bottom, paint)
        }
    }
}