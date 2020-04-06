package com.bootcamp.bootcampmagic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.adapter.AdapterCards
import com.bootcamp.bootcampmagic.models.Card

class SetsFragment : Fragment() {

    private lateinit var listCards: RecyclerView
    private lateinit var searchCards: EditText
    private lateinit var btnCancelar: TextView
    private lateinit var recyclerCards: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_set, container, false)

        listCards = view.findViewById(R.id.recycler_cards)
        searchCards = view.findViewById(R.id.search_cards)
        btnCancelar = view.findViewById(R.id.btn_cancelar)
        recyclerCards = view.findViewById(R.id.recycler_cards)

        setupRecyclerView(listOf(Card("Teste", "Teste 2", "asd", "asdsad", "qwqewqe", false)))
        return view
    }

    private fun setupRecyclerView(listCards: List<Card>){
        val adapterCards = AdapterCards(listCards)
        val gridLayoutManager = GridLayoutManager(context, 3)

        recyclerCards.layoutManager = gridLayoutManager
        recyclerCards.adapter = adapterCards
    }
}