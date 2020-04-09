package com.bootcamp.bootcampmagic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.adapter.AdapterCards
import com.bootcamp.bootcampmagic.adapter.EndlessScrollListener
import com.bootcamp.bootcampmagic.adapter.GridSpacingItemDecoration
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.repositories.CardsRepository
import com.bootcamp.bootcampmagic.utils.App
import com.bootcamp.bootcampmagic.viewmodels.SetsViewModel
import com.bootcamp.bootcampmagic.viewmodels.SetsViewModelFactory
import com.bootcamp.bootcampmagic.viewmodels.SetsViewModelState
import kotlinx.android.synthetic.main.collapsing_toolbar.*
import com.google.android.material.snackbar.Snackbar
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

        search_cards.doOnTextChanged { text, start, count, after ->
            viewModel.searchCard(text.toString())
        }

        btn_cancelar.setOnClickListener {
            search_cards.setText("")
            viewModel.refresh()
        }

        setupRecyclerView()
        setupObservables()
    }


    private fun setupRecyclerView(){

        adapterCards = AdapterCards(clickListener)
        val spanCount = 3
        val layoutManager = GridLayoutManager(context, spanCount)
        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if (adapterCards.isHeader(position)) layoutManager.spanCount else 1
            }
        }
        recycler_cards.layoutManager = layoutManager
        recycler_cards.addItemDecoration(GridSpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.grid_item_margin)))
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

        swipeRefresh.setOnRefreshListener {
            refresh()
            swipeRefresh.isRefreshing = false
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
                    when(it.message){
                        R.string.generic_network_error -> showNetworkError(it.message)
                    }

                is SetsViewModelState.CacheLoaded -> {
                    refresh()
                }

                is SetsViewModelState.loadinContent ->{
                    recycler_cards.visibility = View.GONE
                    loadingContent.visibility = View.VISIBLE
                    adapterCards.isSearhing()
                }
            }
        })

        viewModel.getBackgroundImage().observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).setBackgroundImage(it)
        })

        viewModel.getData().observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()){
                loadingContent.visibility = View.GONE
                recycler_cards.visibility = View.VISIBLE
                when(isRefreshing){
                    true -> {
                        adapterCards.setItems(it)
                    }
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

    private fun showNetworkError(errorMessage: Int){
        activity?.findViewById<View>(R.id.tab_set_favorites)?.let {
            Snackbar.make(it, errorMessage, Snackbar.LENGTH_LONG)
                .setAnchorView(it)
                .setAction(R.string.try_again) {
                    viewModel.loadCards()
                }
                .show()
        }
    }
}