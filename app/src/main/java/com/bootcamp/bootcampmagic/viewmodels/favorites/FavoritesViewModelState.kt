package com.bootcamp.bootcampmagic.viewmodels.favorites

import androidx.annotation.StringRes
import com.bootcamp.bootcampmagic.models.ListItem

sealed class FavoritesViewModelState(){
    data class Error(@StringRes val message: Int): FavoritesViewModelState()
}