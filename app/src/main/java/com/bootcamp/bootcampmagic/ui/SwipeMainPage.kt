package com.bootcamp.bootcampmagic.ui

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class SwipeMainPage(
    val viewPager: ViewPager,
    val tabLayout: TabLayout,
    val pagerAdapter: PagerAdapter
){

    fun init(){
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

}