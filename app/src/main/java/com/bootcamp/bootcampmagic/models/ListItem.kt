package com.bootcamp.bootcampmagic.models

interface ListItem {
    companion object{

        const val CARD = 1
        const val SET = 2
        const val CARD_TYPE = 3

    }
    fun getItemType(): Int
}