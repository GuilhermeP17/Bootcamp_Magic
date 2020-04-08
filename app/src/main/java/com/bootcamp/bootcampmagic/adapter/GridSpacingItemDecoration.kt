package com.bootcamp.bootcampmagic.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    spacing: Int
) : RecyclerView.ItemDecoration() {

    private val itemMargin = when(spacing){
        0 -> 0
        1 -> 0
        else -> (spacing /2)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = itemMargin
        outRect.right = itemMargin
        outRect.top = itemMargin
        outRect.bottom = itemMargin
    }
}