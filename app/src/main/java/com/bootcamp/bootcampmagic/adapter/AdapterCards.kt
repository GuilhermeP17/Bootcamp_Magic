package com.bootcamp.bootcampmagic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.models.Card
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.adapter_cards.view.*

class AdapterCards(
    private val itemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<AdapterCards.CardViewHolder>(){

    private val listCards: MutableList<Card> = mutableListOf()


    fun setItems(items: List<Card>){
        if(listCards.isEmpty()){

            listCards.addAll(items)
            notifyItemRangeChanged(0, items.size)

        }else{

            val totalItems = listCards.size
            listCards.clear()
            listCards.addAll(items)
            notifyItemRangeChanged(0, totalItems)

        }
    }
    fun addItems(items: List<Card>){
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(items, listCards))
        diffResult.dispatchUpdatesTo(this)
        listCards.addAll(items)
    }
    override fun getItemCount(): Int {
        return listCards.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_cards, parent, false)
        return CardViewHolder(view)
    }
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listCards[position], itemClickListener)
    }


    class CardViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(card: Card, clickListener: OnItemClickListener?) {

            if (card.imageUrl == "..."){
                Glide.with(itemView)
                    .load(R.drawable.no_card)
                    .into(view.img_card)
            }else {
                Glide.with(itemView.context)
                    .load(card.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view.img_card)
            }

            clickListener?.let {listener ->
                view.setOnClickListener {
                    listener.onItemClicked(card, adapterPosition)
                }
            }

        }
    }


    private class MyDiffCallback(
        private val newList: List<Card>,
        private val oldList: List<Card>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id === newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        @Nullable
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }

    interface OnItemClickListener{
        fun onItemClicked(card: Card, position: Int)
    }
}