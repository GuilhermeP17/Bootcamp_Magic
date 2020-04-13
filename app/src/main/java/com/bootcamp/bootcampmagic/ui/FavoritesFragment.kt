package com.bootcamp.bootcampmagic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.repositories.MtgRepository
import com.bootcamp.bootcampmagic.utils.App
import com.bootcamp.bootcampmagic.viewmodels.FavoritesViewModel
import com.bootcamp.bootcampmagic.viewmodels.FavoritesViewModelFactory

class FavoritesFragment : Fragment(){

    private val viewModel: FavoritesViewModel by viewModels{
        App().let {
            FavoritesViewModelFactory(MtgRepository(it.getCardsDao(), it.getMtgDataSource()))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupObservables()
    }

    private fun setupObservables(){

    }
}