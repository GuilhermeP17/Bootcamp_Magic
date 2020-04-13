package com.bootcamp.bootcampmagic.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CarouselView(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    fun <T: ViewHolder> initialize(newAdapter: Adapter<T>) {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)


        this.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                (0 until childCount).forEach { position ->
                    val child = getChildAt(position)
                    val childCenterX = (child.left + child.right) / 2
                    val scaleValue = getGaussianScale(childCenterX, 1f, 1f, 150.toDouble())
                    child.scaleX = scaleValue
                    child.scaleY = scaleValue
                }
            }
        })
        adapter = newAdapter
    }

    private fun getGaussianScale(
        childCenterX: Int,
        minScaleOffest: Float,
        scaleFactor: Float,
        spreadFactor: Double
    ): Float {
        val recyclerCenterX = (left + right) / 2
        return (Math.pow(
            Math.E,
            -Math.pow(childCenterX - recyclerCenterX.toDouble(), 2.toDouble()) / (2 * Math.pow(
                spreadFactor,
                2.toDouble()
            ))
        ) * scaleFactor + minScaleOffest).toFloat()
    }

}