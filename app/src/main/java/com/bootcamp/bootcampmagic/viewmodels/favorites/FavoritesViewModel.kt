package com.bootcamp.bootcampmagic.viewmodels.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bootcamp.bootcampmagic.models.Favorite
import com.bootcamp.bootcampmagic.repositories.FavoritesRepository
import com.bootcamp.bootcampmagic.utils.DefaultDispatcherProvider
import com.bootcamp.bootcampmagic.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: FavoritesRepository,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel(){

    private val state = MutableLiveData<FavoritesViewModelState>()
    private val data = MutableLiveData<MutableList<Favorite>>()

    init {
        getFavorites()
    }

    fun getViewState(): MutableLiveData<FavoritesViewModelState> = state
    fun getData(): MutableLiveData<MutableList<Favorite>> = data

    fun getFavorites(){
        try{
            CoroutineScope(dispatchers.io()).launch {
                repository.getFavorites().let { list ->
                    if (list.isNotEmpty()){
                        data.value = list.toMutableList()
                    }
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}