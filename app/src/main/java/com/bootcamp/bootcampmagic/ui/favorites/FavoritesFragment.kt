package com.bootcamp.bootcampmagic.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.repositories.FavoritesRepository
import com.bootcamp.bootcampmagic.utils.App
import com.bootcamp.bootcampmagic.viewmodels.favorites.FavoritesViewModel
import com.bootcamp.bootcampmagic.viewmodels.favorites.FavoritesViewModelFactory
import kotlinx.android.synthetic.main.collapsing_toolbar.*

class FavoritesFragment : Fragment(){

    private val viewModel: FavoritesViewModel by viewModels{
        App().let {
            FavoritesViewModelFactory(
                FavoritesRepository(it.getFavoriteCardsDao())
            )
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

        btn_cancelar.setOnClickListener {
            viewModel.getFavorites()
        }

        setupObservables()
    }

    private fun setupObservables(){
        viewModel.getViewState().observe(viewLifecycleOwner, Observer { state ->
            //TODO
        })

        viewModel.getData().observe(viewLifecycleOwner, Observer {
            //TODO
        })
    }
}