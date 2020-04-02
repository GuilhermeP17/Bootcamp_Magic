package com.bootcamp.bootcampmagic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.bootcampmagic.R

class SetsFragment : Fragment() {

    private lateinit var listCards: RecyclerView
    private lateinit var searchCards: EditText
    private lateinit var btnCancelar: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_set, container, false)

        listCards = view.findViewById(R.id.recycler_cards)
        searchCards = view.findViewById(R.id.search_cards)
        btnCancelar = view.findViewById(R.id.btn_cancelar)

        return view
    }

}