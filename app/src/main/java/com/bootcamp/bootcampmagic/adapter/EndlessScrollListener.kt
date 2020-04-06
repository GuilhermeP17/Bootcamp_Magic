package com.bootcamp.bootcampmagic.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener(
    recyclerView: RecyclerView
) {

    private val loadMorePercentage = 80
    private var currentPosition: Position = Position.START
    private val layoutManager: LinearLayoutManager = (recyclerView.layoutManager as LinearLayoutManager)


    init {

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)


                //Check if reached the first item
                val firstItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                if(firstItem == 0){
                    if(currentPosition != Position.START){
                        onFirstItem()
                    }
                    currentPosition = Position.START
                    return
                }


                //Check if reached the load more position
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                val lastItem = layoutManager.findLastVisibleItemPosition()
                val currentPercentage = ((totalItemCount * loadMorePercentage) / 100)
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


    abstract fun onFirstItem()
    abstract fun onScroll()
    abstract fun onLoadMore()

    fun reset(){
        currentPosition = Position.SCROLLING
    }

    private enum class Position(val value: Int) {
        START(1),
        SCROLLING(2),
        LOAD_MORE(3)
    }

}
