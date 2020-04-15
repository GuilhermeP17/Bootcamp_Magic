package com.bootcamp.bootcampmagic.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.CustomTarget
import jp.wasabeef.glide.transformations.BlurTransformation

class GlideWithBlur{
    private val blurRadius = 2
    private val blurSampling = 3
    private val width = 100
    private val height = 300

    fun setImage(context: Context, imageUrl: String, view: ImageView){
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(bitmapTransform(BlurTransformation(blurRadius, blurSampling)))
            .override(width, height)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                ) {
                    view.setImageBitmap(resource)
                }
            })
    }
    fun setImage(context: Context, imageResource: Int, view: ImageView){
        Glide.with(context)
            .load(imageResource)
            .apply(bitmapTransform(BlurTransformation(blurRadius, blurSampling)))
            .override(width, height)
            .into(view)
    }

}