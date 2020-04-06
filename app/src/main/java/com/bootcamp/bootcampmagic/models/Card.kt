package com.bootcamp.bootcampmagic.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Card (
    @PrimaryKey val multiverseid: Long,
    val name: String,
    val type: String,
    val set: String,
    val setName: String,
    val imageUrl: String,
    var favorite: Boolean = false
)