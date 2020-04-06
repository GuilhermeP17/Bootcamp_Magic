package com.bootcamp.bootcampmagic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.models.CardsResponse
import com.bootcamp.bootcampmagic.repositories.CardsRepository
import kotlinx.coroutines.*
import java.net.HttpURLConnection

class SetsViewModel (
    private val repository: CardsRepository
): ViewModel() {

    private val state = MutableLiveData<SetsViewModelState>()
    private var page = 1
    private val data = MutableLiveData<MutableList<Card>>().apply {
        value = mutableListOf()
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            repository.getCache().let {
                withContext(Dispatchers.Main) {
                    setCards(it)
                }
            }
            loadCards()
        }
    }

    fun getViewState(): LiveData<SetsViewModelState> = state
    fun getPage(): Int = (page - 1)
    fun getData(): MutableLiveData<MutableList<Card>> = data

    fun refresh(){
        page = 1
        loadCards()
    }

    fun loadCards(){
        CoroutineScope(Dispatchers.Default).launch {
            repository.getCards(page).let {
                withContext(Dispatchers.Main) {
                    setCards(it)
                }
            }
        }
    }
    private fun setCards(cardsResponse: CardsResponse){
        cardsResponse.let {
            when(it.errorCode){
                HttpURLConnection.HTTP_OK ->
                    if(it.cards.isNotEmpty()){
                        if(page == 1){
                            data.value?.clear()
                        }
                        data.value?.addAll(it.cards)
                        page++
                        data.notifyObserver()
                    }


                else ->
                    state.value = SetsViewModelState.Error(R.string.generic_network_error)
            }
        }
    }


    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

}