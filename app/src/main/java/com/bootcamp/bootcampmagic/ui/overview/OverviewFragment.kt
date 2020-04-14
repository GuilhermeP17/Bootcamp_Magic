package com.bootcamp.bootcampmagic.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.adapter.CarouselAdapter
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.models.ListType
import com.bootcamp.bootcampmagic.utils.SharedViewModel
import com.bootcamp.bootcampmagic.viewmodels.favorites.FavoritesViewModelFactory
import com.bootcamp.bootcampmagic.viewmodels.favorites.FavoritesViewModelState
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModelFactory
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModelState
import com.bootcamp.bootcampmagic.views.CarouselView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_overview.*

class OverviewFragment: Fragment() {

    private val adapter = CarouselAdapter()
    private var viewModel: SharedViewModel? = null
    private val args: OverviewFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(args.listType){
            ListType.SETS.value -> {
                viewModel = SetsViewModelFactory.getViewModelStance()
            }

            ListType.FAVORITES.value -> {
                viewModel = FavoritesViewModelFactory.getViewModelStance()
            }
        }


        setupCarousel()
        setupViewModel()

        btnFavorite.setOnClickListener{
            setFavorite()
        }
        closeButton.setOnClickListener {
            view.let {
                Navigation.findNavController(it).popBackStack()
            }
        }
    }


    private fun setupCarousel(){

        carouselView.initialize(adapter)
        carouselView.setOnItemSelectedListener(object: CarouselView.OnItemSelectedListener{
            override fun onItemSelected(position: Int) {
                setSelectedItem(position)
                val card: Card = adapter.getItem(position) as Card
                setButtonFavoriteState(card.favorite)
            }
        })

    }


    private fun setupViewModel(){
        viewModel?.let {
            carouselView.scrollToPosition(it.getSelectedItem())

            it.getSetsViewModelState()?.observe(viewLifecycleOwner, Observer { state ->
                if(state == null) return@Observer
                when(state){

                    is SetsViewModelState.Error ->
                        when(state.message){
                            R.string.generic_network_error -> showNetworkError(state.message)
                        }


                    is SetsViewModelState.AddData ->{
                        adapter.addItems(state.items)
                    }

                }
                it.clearViewState()
            })

            it.getFavoritesViewModelState()?.observe(viewLifecycleOwner, Observer { state ->
                if(state == null) return@Observer
                when(state){

                    is FavoritesViewModelState.Error ->
                        when(state.message){
                            R.string.generic_network_error -> showNetworkError(state.message)
                        }

                }
                it.clearViewState()
            })


            it.getData().observe(viewLifecycleOwner, Observer { items ->
                if(items.isNotEmpty()){
                    adapter.setItems(items)
                }
            })
        }
    }


    private fun setSelectedItem(position: Int){
        viewModel?.setSelectedItem(position)
    }


    private fun setFavorite(){
        viewModel?.let {
            val position = it.getSelectedItem()
            when(adapter.getItem(position)){
                is Card -> {
                    val card: Card = adapter.getItem(position) as Card
                    val favorite = (!card.favorite)

                    it.setFavorite(position, favorite)
                    adapter.setFavorite(position, favorite)
                    setButtonFavoriteState(favorite)
                }
            }
        }
    }
    private fun setButtonFavoriteState(favorite: Boolean){
        if(favorite){
            btnFavorite.text = getString(R.string.remove_favorite)
        }else{
            btnFavorite.text = getString(R.string.add_favorite)
        }
    }


    private fun showNetworkError(errorMessage: Int){
        view?.let {
            Snackbar.make(it, errorMessage, Snackbar.LENGTH_LONG)
                .setAction(R.string.try_again) {
                    viewModel?.loadMore()
                }
                .show()
        }
    }
}