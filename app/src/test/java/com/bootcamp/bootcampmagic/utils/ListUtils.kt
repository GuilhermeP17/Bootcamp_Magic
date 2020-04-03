package com.bootcamp.bootcampmagic.utils

import com.bootcamp.bootcampmagic.models.Card

object ListUtils {

    fun createCardsList(size: Int): List<Card>{
        return ArrayList<Card>().apply {
            for (index in 1..size){
                add(Card(index.toLong(), "name $index", "type $index","set $index","setNAme $index","imageUrl $index"))
            }
        }
    }

}