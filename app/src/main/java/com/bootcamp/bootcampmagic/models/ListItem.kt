package com.bootcamp.bootcampmagic.models

interface ListItem {
    companion object{
        val ITEM = 1
        val HEADER = 0
    }

    fun getItemType(): Int
}