package com.bootcamp.bootcampmagic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.models.ItemHeader
import com.bootcamp.bootcampmagic.models.ListItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.adapter_cards.view.*
import kotlinx.android.synthetic.main.adapter_title.view.*

class AdapterCards(
    private val itemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<AdapterCards.CardViewHolder>(){

    private val listCards: MutableList<ListItem> = mutableListOf()
    private var currentType = ""

    fun setItems(items: List<Card>){
        currentType = ""
        if(listCards.isEmpty()){
            listCards.addAll(cardToListItem(items))
            notifyItemRangeChanged(0, items.size)

        }else{

            val totalItems = listCards.size
            listCards.clear()
            listCards.addAll(cardToListItem(items))
            notifyItemRangeChanged(0, totalItems)

        }
    }
    fun addItems(items: List<Card>){
        //val diffResult = DiffUtil.calculateDiff(MyDiffCallback(cardToListItem(items), listCards))
        //diffResult.dispatchUpdatesTo(this)
        val totalItems = listCards.size
        listCards.addAll(cardToListItem(items))
        notifyItemRangeChanged(totalItems, items.size)
    }

    fun isSearhing(){
        listCards.clear()
    }

    override fun getItemCount(): Int {
        return listCards.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        if(viewType == ListItem.HEADER){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_title, parent, false)
            return CardViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_cards, parent, false)
        return CardViewHolder(view)
    }
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listCards[position], itemClickListener)
    }


    class CardViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ListItem, clickListener: OnItemClickListener?) {
            if(item !is Card){
                view.itemTitle.text = (item as ItemHeader).title
                return
            }

            val card = (item as Card)
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

    override fun getItemViewType(position: Int): Int {
        return if (isHeader(position)) ListItem.HEADER else ListItem.ITEM
    }
    fun isHeader(position: Int): Boolean {
        return listCards[position].getItemType() == ListItem.HEADER
    }

    private fun cardToListItem(listCards: List<Card>): List<ListItem>{
        val newList = mutableListOf<ListItem>()
        for(item in listCards){
            val title = item.type //+ " Set: (${item.set})"
            if(title != currentType){
                currentType = title
                newList.add(ItemHeader(currentType))
            }
            newList.add(item)
        }
        return newList
    }

    private class MyDiffCallback(
        private val newList: List<ListItem>,
        private val oldList: List<ListItem>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return when(oldList[oldItemPosition]){
                is Card ->
                    (oldList[oldItemPosition] as Card).id === (newList[newItemPosition] as Card).id
                else -> false
            }
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