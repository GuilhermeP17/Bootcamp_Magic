package com.bootcamp.bootcampmagic.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.models.Card
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

class CarouselAdapter: RecyclerView.Adapter<CarouselAdapter.ViewHolder>(){

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
            itemList.clear()
            itemList.addAll(items)
            notifyItemRangeChanged(0, items.size)
        }
    }
    fun addItems(items: List<ListItem>){
        val totalItems = itemList.size
        itemList.addAll(items)
        notifyItemRangeChanged(totalItems, items.size)
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
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.empty_item, parent, false))

            ListItem.SET ->
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.empty_item, parent, false))

            else ->
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false))
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ListItem) {
            when (item){

                is Card ->{
                    if (item.imageUrl.isEmpty()){
                        Glide.with(itemView)
                            .load(R.drawable.no_card)
                            .apply(RequestOptions().optionalTransform(RoundedCorners(16)))
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
                            .apply(RequestOptions().optionalTransform(RoundedCorners(16)))
                            .into(view.img_card)
                    }
                }
            }
        }
    }
}