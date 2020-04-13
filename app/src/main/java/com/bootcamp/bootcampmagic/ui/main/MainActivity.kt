package com.bootcamp.bootcampmagic.ui.main

import android.os.Bundle
import com.bootcamp.bootcampmagic.R

class MainActivity : ImmersiveActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBackgroundLayout(R.layout.immersive_background)
        setBackgroundImage(R.drawable.default_background)
    }

}