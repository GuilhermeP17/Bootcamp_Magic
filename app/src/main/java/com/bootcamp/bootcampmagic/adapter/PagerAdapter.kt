package com.bootcamp.bootcampmagic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(
    fragmentManager: FragmentManager,
    private val fragments: ArrayList<Fragment>,
    private val titulos: ArrayList<String>
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence{
        return titulos[position]
    }

    fun adicionarFragment(fragment: Fragment, titulo: String){
        fragments.add(fragment)
        titulos.add(titulo)
    }
}