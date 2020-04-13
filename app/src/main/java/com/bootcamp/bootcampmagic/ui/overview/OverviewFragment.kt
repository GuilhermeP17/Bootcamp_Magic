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
import com.bootcamp.bootcampmagic.models.ListType
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModel
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModelFactory
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModelState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_overview.*

class OverviewFragment: Fragment() {

    private val adapter = CarouselAdapter()
    private var setsViewModel: SetsViewModel? = null
    private val args: OverviewFragmentArgs by navArgs()


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

        carouselView.initialize(adapter)
        when(args.listType){
            ListType.SETS.value -> {
                setsViewModel = SetsViewModelFactory.getViewModelStance()
                setsViewModel?.let { viewModel ->
                    setupSetsViewModel(viewModel)

                }
            }

        }
    }


    private fun setupSetsViewModel(viewModel: SetsViewModel){
        viewModel.getViewState().observe(viewLifecycleOwner, Observer { state ->
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
            viewModel.clearViewState()
        })


        viewModel.getData().observe(viewLifecycleOwner, Observer { items ->
            if(items.isNotEmpty()){
                adapter.setItems(items)
                viewModel.selectedItem.value?.let { position ->
                    carouselView.scrollToPosition(position)
                }
            }
        })
    }


    private fun showNetworkError(errorMessage: Int){
        view?.let {
            Snackbar.make(it, errorMessage, Snackbar.LENGTH_LONG)
                .setAction(R.string.try_again) {
                    when(args.listType){

                        ListType.SETS.value -> setsViewModel?.loadMore()

                    }
                }
                .show()
        }
    }
}