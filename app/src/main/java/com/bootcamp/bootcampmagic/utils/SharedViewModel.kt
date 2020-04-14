package com.bootcamp.bootcampmagic.utils

import androidx.lifecycle.MutableLiveData
import com.bootcamp.bootcampmagic.models.ListItem
import com.bootcamp.bootcampmagic.viewmodels.favorites.FavoritesViewModelState
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModelState

interface SharedViewModel{
    fun loadMore()
    fun setFavorite(position: Int, favorite: Boolean)
    fun getData(): MutableLiveData<MutableList<ListItem>>
    fun getSelectedItem(): Int
    fun setSelectedItem(value: Int)
    fun clearViewState()
    fun getSetsViewModelState(): MutableLiveData<SetsViewModelState>?
    fun getFavoritesViewModelState(): MutableLiveData<FavoritesViewModelState>?
}