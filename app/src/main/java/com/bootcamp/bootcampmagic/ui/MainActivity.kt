package com.bootcamp.bootcampmagic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.adapter.PagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.container_main)
        tabLayout = findViewById(R.id.tab_set_favorites)

        SwipeMainPage(viewPager, tabLayout, setPagerAdapter()).init()
    }

    private fun setPagerAdapter(): PagerAdapter{
        val pagerAdapter = PagerAdapter(supportFragmentManager, ArrayList(), ArrayList())
        pagerAdapter.adicionarFragment(SetsFragment(), "Sets")
        pagerAdapter.adicionarFragment(Fragment(), "Favoritos")

        return pagerAdapter
    }
}
