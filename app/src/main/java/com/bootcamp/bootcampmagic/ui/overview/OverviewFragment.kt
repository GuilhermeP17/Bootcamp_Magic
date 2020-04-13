package com.bootcamp.bootcampmagic.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bootcamp.bootcampmagic.R
import kotlinx.android.synthetic.main.fragment_overview.*

class OverviewFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        closeButton.setOnClickListener {
            view?.let {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }
}