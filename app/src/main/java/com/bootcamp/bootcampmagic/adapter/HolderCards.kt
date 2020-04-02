package com.bootcamp.bootcampmagic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.bootcampmagic.R

class HolderCards(
    inflater: LayoutInflater,
    parent: ViewGroup
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.adapter_cards, parent, false)) {

    private var imgCard: ImageView? = null

    init {
        imgCard = itemView.findViewById(R.id.img_card)
    }

}