package com.bootcamp.bootcampmagic.models

data class CardType(
    val name: String,
    val types: List<String>
): ListItem{

    override fun getItemType(): Int = ListItem.CARD_TYPE

}