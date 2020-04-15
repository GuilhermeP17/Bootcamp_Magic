package com.bootcamp.bootcampmagic.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Card (
    @PrimaryKey val id: String,
    val name: String,
    var type: String,
    @Ignore val types: List<String>,
    val set: String,
    val setName: String,
    var imageUrl: String = "",
    var favorite: Boolean = false,
    var isCache: Boolean = false
): ListItem{
    constructor(
        id: String,
        name: String,
        type: String,
        set: String,
        setName: String,
        imageUrl: String = "",
        favorite: Boolean = false,
        isCache: Boolean = false
    ) : this(
        id,
        name,
        type,
        mutableListOf<String>(),
        set,
        setName,
        imageUrl,
        favorite,
        isCache)

    override fun getItemType(): Int = ListItem.CARD
}