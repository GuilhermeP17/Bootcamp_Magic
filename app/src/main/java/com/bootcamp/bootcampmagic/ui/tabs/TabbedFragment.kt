package com.bootcamp.bootcampmagic.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.adapter.PagerAdapter
import com.bootcamp.bootcampmagic.ui.favorites.FavoritesFragment
import com.bootcamp.bootcampmagic.ui.sets.SetsFragment
import kotlinx.android.synthetic.main.fragment_tabs.*

class TabbedFragment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = PagerAdapter(childFragmentManager, ArrayList(), ArrayList())
        pagerAdapter.adicionarFragment(SetsFragment(), getString(R.string.titulo_tab_sets))
        pagerAdapter.adicionarFragment(FavoritesFragment(), getString(R.string.titulo_tab_favoritos))

        SwipeMainPage(
            viewPager,
            tabLayout,
            pagerAdapter
        ).init()
    }
}