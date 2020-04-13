package com.bootcamp.bootcampmagic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.repositories.MtgRepository

class FavoritesViewModel(
    private val repository: MtgRepository
) : ViewModel(){

    private val state = MutableLiveData<FavoritesViewModelState>()
    private val data = MutableLiveData<MutableList<Card>>()

    fun getViewState(): MutableLiveData<FavoritesViewModelState> = state
    fun getData(): MutableLiveData<MutableList<Card>> = data

    fun getFavorites(){

    }
}