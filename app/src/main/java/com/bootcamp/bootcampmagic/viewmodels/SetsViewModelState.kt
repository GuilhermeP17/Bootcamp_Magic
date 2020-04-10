package com.bootcamp.bootcampmagic.viewmodels

import androidx.annotation.StringRes
import com.bootcamp.bootcampmagic.models.ListItem

sealed class SetsViewModelState {
    data class Error(@StringRes val message: Int): SetsViewModelState()
    data class BackgroundImage(val url: String): SetsViewModelState()
    object CacheLoaded: SetsViewModelState()
    data class AddData(val items: List<ListItem>): SetsViewModelState()
}