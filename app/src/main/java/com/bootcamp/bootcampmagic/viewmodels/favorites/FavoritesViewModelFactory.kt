package com.bootcamp.bootcampmagic.viewmodels.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bootcamp.bootcampmagic.repositories.MtgRepository

class FavoritesViewModelFactory(
    private val repository: MtgRepository
) : ViewModelProvider.Factory{
    companion object{
        private var viewModel: FavoritesViewModel? = null
        fun getViewModelStance() = viewModel
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            if(viewModel == null){
                viewModel = FavoritesViewModel(repository)
            }
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}