package com.bootcamp.bootcampmagic.viewmodels.favorites

import androidx.annotation.StringRes

sealed class FavoritesViewModelState(){
    data class Error(@StringRes val message: Int): FavoritesViewModelState()
}