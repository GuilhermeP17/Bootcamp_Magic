package com.bootcamp.bootcampmagic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.adapter.AdapterCards
import com.bootcamp.bootcampmagic.adapter.EndlessScrollListener
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.repositories.CardsRepository
import com.bootcamp.bootcampmagic.utils.App
import com.bootcamp.bootcampmagic.viewmodels.SetsViewModel
import com.bootcamp.bootcampmagic.viewmodels.SetsViewModelFactory
import com.bootcamp.bootcampmagic.viewmodels.SetsViewModelState
import kotlinx.android.synthetic.main.fragment_set.*

class SetsFragment() : Fragment() {

    private lateinit var adapterCards: AdapterCards
    private var isRefreshing = true
    private val viewModel: SetsViewModel by viewModels{
        App().let {
            SetsViewModelFactory(CardsRepository(it.getCardsDataSource(), it.getCardsDao()))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //recycler_cards.visibility = View.GONE
        setupRecyclerView()
        setupObservables()
    }


    private fun setupRecyclerView(){

        adapterCards = AdapterCards(clickListener)
        recycler_cards.layoutManager = GridLayoutManager(context, 3)
        recycler_cards.adapter = adapterCards

        val endlessScrollListener = object : EndlessScrollListener(recycler_cards){
            override fun onFirstItem() {
            }
            override fun onScroll() {
            }
            override fun onLoadMore() {
                viewModel.loadCards()
            }
        }

    }

    private val clickListener = object: AdapterCards.OnItemClickListener{
        override fun onItemClicked(card: Card, position: Int) {
        }
    }

    private fun setupObservables(){
        viewModel.getViewState().observe(viewLifecycleOwner, Observer {
            when(it){

                is SetsViewModelState.Error ->
                    showErrorMessage(it.toString())

                is SetsViewModelState.CacheLoaded -> {
                    progress_circular.visibility = View.GONE
                    refresh()
                }

            }
        })
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()){
                when(isRefreshing){
                    true -> adapterCards.setItems(it)
                    else -> adapterCards.addItems(it)
                }
                isRefreshing = false
            }
        })
    }

    private fun refresh(){
        isRefreshing = true
        viewModel.refresh()
    }

    private fun showErrorMessage(errorMessage: String){
        progress_circular.visibility = View.GONE
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
}