package com.bootcamp.bootcampmagic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.adapter.AdapterCards
import com.bootcamp.bootcampmagic.adapter.EndlessScrollListener
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.repositories.CardsRepository
import com.bootcamp.bootcampmagic.utils.App
import com.bootcamp.bootcampmagic.viewmodels.SetsViewModel
import com.bootcamp.bootcampmagic.viewmodels.SetsViewModelFactory
import kotlinx.android.synthetic.main.fragment_set.*

class SetsFragment() : Fragment() {

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

        setupObservables()
        setupRecyclerView(viewModel.getData().value!!)
    }


    private fun setupObservables(){
        viewModel.getViewState().observe(viewLifecycleOwner, Observer {
            showErrorMessage(it.toString())
        })
        viewModel.getData().observe(viewLifecycleOwner, Observer {

            //TO-DO
            // change to add items


        })
    }

    private fun setupRecyclerView(listCards: List<Card>){
        val adapterCards = AdapterCards(listCards)
        val gridLayoutManager = GridLayoutManager(context, 3)

        recycler_cards.layoutManager = gridLayoutManager
        recycler_cards.adapter = adapterCards
        val endlessScroll = object: EndlessScrollListener(gridLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.loadCards()
            }
        }

    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
}