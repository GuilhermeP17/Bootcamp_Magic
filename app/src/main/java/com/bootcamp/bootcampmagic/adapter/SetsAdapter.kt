package com.bootcamp.bootcampmagic.adapter

import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.adapter_cards.view.*
import kotlinx.android.synthetic.main.sets_title.view.*
import kotlinx.android.synthetic.main.types_title.view.*


class SetsAdapter(
    private val clickListener: OnItemClickListener? = null
): RecyclerView.Adapter<SetsAdapter.ViewHolder>(){

    private var itemList: MutableList<ListItem> = mutableListOf()



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
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.types_title, parent, false))

            ListItem.SET ->
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sets_title, parent, false))

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
                    view.setsTitle.text = item.name
                }


                is CardType ->{
                    view.typesTitle.text = item.name
                }


                is Card ->{
                    if (item.imageUrl.isEmpty()){
                        Glide.with(itemView)
                            .load(R.drawable.no_card)
                            .apply(RequestOptions().transform(RoundedCorners(16)))
                            .into(view.img_card)
                        view.progressBar.visibility = View.GONE
                    }else {
                        Glide.with(itemView.context)
                            .load(item.imageUrl)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    view.progressBar.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    view.progressBar.visibility = View.GONE
                                    return false
                                }

                            })
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .apply(RequestOptions().transform(RoundedCorners(16)))
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