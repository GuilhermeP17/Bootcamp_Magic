package com.bootcamp.bootcampmagic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.adapter.AdapterCards
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.viewmodels.SetsViewModel

class SetsFragment(private val viewModel: SetsViewModel) : Fragment() {

    private lateinit var searchCards: EditText
    private lateinit var btnCancelar: TextView
    private lateinit var recyclerCards: RecyclerView

    //private val listCards

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_set, container, false)

        searchCards = view.findViewById(R.id.search_cards)
        btnCancelar = view.findViewById(R.id.btn_cancelar)
        recyclerCards = view.findViewById(R.id.recycler_cards)

        setupObservables()

        viewModel.loadCards()
        return view
    }

    private fun setupObservables(){
        viewModel.getViewState().observe(viewLifecycleOwner, Observer {
            showErrorMessage(it.toString())
        })

        viewModel.getData().observe(viewLifecycleOwner, Observer {
            setupRecyclerView(it)
        })
    }

    private fun setupRecyclerView(listCards: List<Card>){
        val adapterCards = AdapterCards(listCards)
        val gridLayoutManager = GridLayoutManager(context, 3)

        recyclerCards.layoutManager = gridLayoutManager
        recyclerCards.adapter = adapterCards
    }

    private fun showErrorMessage(errorMessage: String){
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
}