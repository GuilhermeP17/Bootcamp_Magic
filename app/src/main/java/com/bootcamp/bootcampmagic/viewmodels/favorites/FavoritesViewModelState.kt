package com.bootcamp.bootcampmagic.viewmodels.favorites

import androidx.annotation.StringRes
import com.bootcamp.bootcampmagic.models.ListItem
import com.bootcamp.bootcampmagic.models.LoadingType

sealed class FavoritesViewModelState(){
    data class Error(@StringRes val message: Int): FavoritesViewModelState()
    data class LoadingState(val type: LoadingType): FavoritesViewModelState()
}