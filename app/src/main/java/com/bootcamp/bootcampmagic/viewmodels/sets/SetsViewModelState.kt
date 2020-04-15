package com.bootcamp.bootcampmagic.viewmodels.sets

import androidx.annotation.StringRes
import com.bootcamp.bootcampmagic.models.ListItem
import com.bootcamp.bootcampmagic.models.LoadingType

sealed class SetsViewModelState {
    data class Error(@StringRes val message: Int): SetsViewModelState()
    data class LoadingState(val type: LoadingType): SetsViewModelState()
    data class BackgroundImage(val url: String): SetsViewModelState()
    data class AddData(val items: List<ListItem>): SetsViewModelState()
}