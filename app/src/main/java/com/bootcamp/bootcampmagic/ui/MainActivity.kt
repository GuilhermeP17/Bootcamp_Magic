package com.bootcamp.bootcampmagic.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.adapter.PagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ImmersiveActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBackgroundLayout(R.layout.immersive_background)
        setBackgroundImage(R.drawable.default_background)

        SwipeMainPage(container_main, tab_set_favorites, setPagerAdapter()).init()
    }

    private fun setPagerAdapter(): PagerAdapter{
        val pagerAdapter = PagerAdapter(supportFragmentManager, ArrayList(), ArrayList())

        pagerAdapter.adicionarFragment(SetsFragment(), getString(R.string.titulo_tab_sets))
        pagerAdapter.adicionarFragment(Fragment(), getString(R.string.titulo_tab_favoritos))

        return pagerAdapter
    }



}