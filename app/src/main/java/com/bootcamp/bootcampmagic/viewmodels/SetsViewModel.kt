package com.bootcamp.bootcampmagic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.models.CardsResponse
import com.bootcamp.bootcampmagic.repositories.CardsRepository
import com.bootcamp.bootcampmagic.utils.DefaultDispatcherProvider
import com.bootcamp.bootcampmagic.utils.DispatcherProvider
import kotlinx.coroutines.*
import java.net.HttpURLConnection

class SetsViewModel (
    private val repository: CardsRepository,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
): ViewModel() {

    private val state = MutableLiveData<SetsViewModelState>()
    private var page = 1
    private val backgroundImage = MutableLiveData<String>()
    private val data = MutableLiveData<MutableList<Card>>().apply {
        value = mutableListOf()
    }

    init {
        CoroutineScope(dispatchers.default()).launch {
            repository.getCache().let {
                withContext(dispatchers.main()) {
                    if(it.isNotEmpty()){
                        data.value?.let {dataList ->
                            dataList.addAll(it)
                            page++
                            data.notifyObserver()
                            //backgroundImage.value = it[it.indices.random()].imageUrl
                        }
                    }
                }
            }
            withContext(dispatchers.main()) {
                state.value = SetsViewModelState.CacheLoaded
            }
        }
    }

    fun getViewState(): LiveData<SetsViewModelState> = state
    fun getPage(): Int = (page - 1)
    fun getData(): MutableLiveData<MutableList<Card>> = data
    fun getBackgroundImage(): MutableLiveData<String> = backgroundImage

    fun refresh(){
        page = 1
        loadCards()
    }

    fun loadCards(){
        CoroutineScope(dispatchers.default()).launch {
            repository.getCards(page).let {
                withContext(dispatchers.main()) {

                    it.let {
                        when(it.errorCode){
                            HttpURLConnection.HTTP_OK ->
                                if(it.cards.isNotEmpty()){
                                    data.value?.let {dataList ->
                                        if(page == 1){
                                            dataList.clear()
                                            backgroundImage.value = it.cards[it.cards.indices.random()].imageUrl
                                        }
                                        dataList.addAll(it.cards)
                                        page++
                                        data.notifyObserver()
                                    }
                                }


                            else ->
                                state.value = SetsViewModelState.Error(R.string.generic_network_error)
                        }
                    }
                }
            }
        }
    }


    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

}