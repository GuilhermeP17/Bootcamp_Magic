package com.bootcamp.bootcampmagic.models

class ItemHeader(
    val title: String
): ListItem{

    override fun getItemType(): Int = ListItem.HEADER

}