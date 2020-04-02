package com.bootcamp.bootcampmagic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterCards(private val listCards: List<String>) : RecyclerView.Adapter<HolderCards>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCards {
        val inflater = LayoutInflater.from(parent.context)
        return HolderCards(inflater, parent)
    }

    override fun getItemCount(): Int {
        return listCards.size
    }

    override fun onBindViewHolder(holder: HolderCards, position: Int) {

    }
}