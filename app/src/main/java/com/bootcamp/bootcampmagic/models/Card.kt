package com.bootcamp.bootcampmagic.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Card (
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val set: String,
    val setName: String,
    var imageUrl: String = "...",
    var favorite: Boolean = false
)