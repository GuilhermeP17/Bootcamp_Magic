package com.bootcamp.bootcampmagic.ui.main

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.bootcamp.bootcampmagic.utils.GlideWithBlur
import kotlinx.android.synthetic.main.immersive_background.*

abstract class ImmersiveActivity : AppCompatActivity(){

    private var backgroundLayoutAttached = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
    }

    fun setBackgroundLayout(layout: Int){
        if(!backgroundLayoutAttached){

            val rootLayout = window.decorView.findViewById<FrameLayout>(android.R.id.content)
            val childrenList = mutableListOf<View>()
            for(view in rootLayout.children){
                childrenList.add(view)
            }
            View.inflate(this, layout, rootLayout)

            //Bring the background view to back
            for(view in childrenList){
                rootLayout.bringChildToFront(view)

            }

            backgroundLayoutAttached = true
        }
    }

    fun setBackgroundImage(imageUrl: String){
        GlideWithBlur().setImage(this, imageUrl, backgroundImage)
    }
    fun setBackgroundImage(imageResource: Int){
        GlideWithBlur().setImage(this, imageResource, backgroundImage)
    }

}