package com.bootcamp.bootcampmagic.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bootcamp.bootcampmagic.repositories.CardsRepository

class SetsViewModelFactory (
    private val repository: CardsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetsViewModel::class.java)) {
            return SetsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}