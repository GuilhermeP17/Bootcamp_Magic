package com.bootcamp.bootcampmagic.views

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlin.math.abs


class CarouselView(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    private val screenWidth: Int = Resources.getSystem().displayMetrics.widthPixels

    fun setOnItemSelectedListener(listener: OnItemSelectedListener){
        selectListener = listener
    }
    private var selectListener: OnItemSelectedListener? = null
    interface OnItemSelectedListener{
        fun onItemSelected(position: Int)
    }


    fun <T: ViewHolder> initialize(newAdapter: Adapter<T>) {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(this)

        this.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                (0 until childCount).forEach { position ->
                    try {
                        val view = getChildAt(position)


                        if(isSelectedView(view)){
                            selectListener?.let { listener ->
                                val listPosition = (layoutManager as LinearLayoutManager).getPosition(view)
                                listener.onItemSelected(listPosition)
                            }
                        }


                        val childWidth: Int = view.right - view.left
                        val childWidthHalf = childWidth / 2f
                        val childCenter: Float = view.left + childWidthHalf

                        val parentWidthHalf = screenWidth / 2f

                        val d0 = 0f
                        val mShrinkDistance = .75f
                        val d1 = mShrinkDistance * parentWidthHalf
                        val s0 = 1f
                        val mShrinkAmount = 0.15f
                        val s1 = 1f - mShrinkAmount

                        val d = d1.coerceAtMost(abs(parentWidthHalf - childCenter))

                        val scaleValue = s0 + (s1 - s0) * (d - d0) / (d1 - d0)

                        view.scaleX = scaleValue
                        view.scaleY = scaleValue

                    } catch (e: Exception) {
                    }
                }
            }

        })
        adapter = newAdapter
    }

    private fun isSelectedView(view: View): Boolean{
        if(view.right >= (screenWidth / 2)){
            if(view.left <= (screenWidth / 2)){
                return true
            }
        }
        return  false
    }

}