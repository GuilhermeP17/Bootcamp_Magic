package com.bootcamp.bootcampmagic.viewmodels.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bootcamp.bootcampmagic.repositories.MtgRepository

class SetsViewModelFactory (
    private val repository: MtgRepository
) : ViewModelProvider.Factory {
    companion object{
        private var viewModel: SetsViewModel? = null
        fun getViewModelStance() = viewModel
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetsViewModel::class.java)) {
            if(viewModel == null){
                viewModel = SetsViewModel(repository)
            }
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}