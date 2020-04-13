package com.bootcamp.bootcampmagic.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bootcamp.bootcampmagic.repositories.FavoritesRepository
import com.bootcamp.bootcampmagic.repositories.MtgRepository

class FavoritesViewModelFactory(
    private val repository: FavoritesRepository
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}