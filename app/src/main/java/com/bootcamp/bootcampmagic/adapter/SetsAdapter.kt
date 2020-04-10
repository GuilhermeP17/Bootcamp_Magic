package com.bootcamp.bootcampmagic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.models.CardSet
import com.bootcamp.bootcampmagic.models.CardType
import com.bootcamp.bootcampmagic.models.ListItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.adapter_cards.view.*
import kotlinx.android.synthetic.main.adapter_title.view.*

class SetsAdapter: RecyclerView.Adapter<SetsAdapter.ViewHolder>(){

    private var itemList: MutableList<ListItem> = mutableListOf()
    private var clickListener: OnItemClickListener? = null


    fun setItems(items: List<ListItem>){
        if (items.isEmpty()){
            val totalItems = itemList.size
            itemList = mutableListOf()
            notifyItemRangeRemoved(0, totalItems)
        }else if(itemList.isEmpty()){
            itemList.addAll(items)
            notifyItemRangeChanged(0, items.size)
        }else{
            /*DiffUtil.calculateDiff(MyDiffCallback(items, itemList))
                .dispatchUpdatesTo(this)*/
            itemList.clear()
            itemList.addAll(items)
            notifyItemRangeChanged(0, items.size)
        }
    }
    fun addItems(items: List<ListItem>){
        /*DiffUtil.calculateDiff(MyDiffCallback(items, itemList))
            .dispatchUpdatesTo(this)*/
        val totalItems = itemList.size
        itemList.addAll(items)
        notifyItemRangeChanged(totalItems, items.size)
    }

    fun setClickListener(clickListener: OnItemClickListener){
        this.clickListener = clickListener
    }

    interface OnItemClickListener{
        fun onItemClicked(card: Card, position: Int)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].getItemType()
    }

    fun isHeader(position: Int): Boolean {
        return itemList[position].getItemType() != ListItem.CARD
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when(viewType){
            ListItem.CARD_TYPE ->
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_title, parent, false))

            ListItem.SET ->
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_title, parent, false))

            else ->
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_cards, parent, false))
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], clickListener)
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ListItem, clickListener: OnItemClickListener?) {
            when (item){

                is CardSet ->{
                    view.itemTitle.text = ("Set  " + item.name)
                }


                is CardType ->{
                    view.itemTitle.text = item.name
                }


                is Card ->{
                    if (item.imageUrl.isEmpty()){
                        Glide.with(itemView)
                            .load(R.drawable.no_card)
                            .into(view.img_card)
                    }else {
                        Glide.with(itemView.context)
                            .load(item.imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(view.img_card)
                    }

                    clickListener?.let { listener ->
                        view.setOnClickListener {
                            listener.onItemClicked(item, adapterPosition)
                        }
                    }
                }
            }
        }
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
                is CardSet ->
                    (oldList[oldItemPosition] as CardSet).name === (newList[newItemPosition] as CardSet).name

                is CardType ->
                    (oldList[oldItemPosition] as CardType).name === (newList[newItemPosition] as CardType).name

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
}