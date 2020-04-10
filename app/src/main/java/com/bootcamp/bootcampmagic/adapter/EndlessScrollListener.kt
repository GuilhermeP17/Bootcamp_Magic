package com.bootcamp.bootcampmagic.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener(
    private val recyclerView: RecyclerView
) : RecyclerView.OnScrollListener() {

    private val layoutManager: RecyclerView.LayoutManager = recyclerView.layoutManager!!
    private val percentage = 70
    private var currentPosition: Position = Position.START

    abstract fun onFirstItem()
    abstract fun onScroll()
    abstract fun onLoadMore()


    init{
        this.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)


                //Check if reached the first item
                val firstItem = when(layoutManager){
                    is LinearLayoutManager ->
                        layoutManager.findFirstCompletelyVisibleItemPosition()
                    is GridLayoutManager ->
                        layoutManager.findFirstCompletelyVisibleItemPosition()
                    else -> 0
                }

                if(firstItem == 0){
                    if(currentPosition != Position.START){
                        onFirstItem()
                    }
                    currentPosition = Position.START
                    return
                }


                //Check if reached the load more position
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                val lastItem = when(layoutManager){
                    is LinearLayoutManager ->
                        layoutManager.findLastVisibleItemPosition()
                    is GridLayoutManager ->
                        layoutManager.findLastVisibleItemPosition()
                    else -> 0
                }

                val currentPercentage = ((totalItemCount * percentage) / 100)
                if(lastItem >= currentPercentage){
                    if(currentPosition != Position.LOAD_MORE){
                        onLoadMore()
                    }

                    currentPosition = Position.LOAD_MORE
                    return
                }


                //Scrolling
                if(currentPosition != Position.LOAD_MORE){
                    onScroll()
                }
                currentPosition = Position.SCROLLING


            }
        })
    }


    fun reset(){
        currentPosition = Position.SCROLLING
    }



    private enum class Position(val value: Int) {
        START(1),
        SCROLLING(2),
        LOAD_MORE(3)
    }
}