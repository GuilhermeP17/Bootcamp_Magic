package com.bootcamp.bootcampmagic.models

data class Card (
    val name: String,
    val type: String,
    val set: String,
    val setName: String,
    val imageUrl: String,
    var favorite: Boolean = false
)