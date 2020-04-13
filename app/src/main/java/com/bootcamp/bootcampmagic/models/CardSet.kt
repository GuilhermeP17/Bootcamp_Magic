package com.bootcamp.bootcampmagic.models

data class CardSet(
    val code: String,
    val name: String
): ListItem{

    override fun getItemType(): Int = ListItem.SET

}