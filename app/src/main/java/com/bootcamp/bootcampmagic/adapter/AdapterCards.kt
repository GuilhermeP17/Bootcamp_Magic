package com.bootcamp.bootcampmagic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.models.Card
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_cards.view.*

class AdapterCards(val listCards: List<Card>) : RecyclerView.Adapter<AdapterCards.CardViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_cards, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCards.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listCards[position])
    }

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.img_card
        private val titleCards = view.title_card

        fun bind(card: Card) {
            if (card.imageUrl == "..."){
                Glide.with(itemView).load(R.drawable.ic_launcher_foreground).into(imageView)
            }else {
                Glide.with(itemView).load(card.imageUrl).into(imageView)
            }

            titleCards.text = card.setName
            titleCards.visibility = View.VISIBLE
        }
    }

}