package com.bootcamp.bootcampmagic.viewmodels.sets

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.models.CardSet
import com.bootcamp.bootcampmagic.models.CardType
import com.bootcamp.bootcampmagic.models.ListItem
import com.bootcamp.bootcampmagic.repositories.MtgRepository
import com.bootcamp.bootcampmagic.utils.DefaultDispatcherProvider
import com.bootcamp.bootcampmagic.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection


class SetsViewModel (
    private val repository: MtgRepository,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
): ViewModel() {

    private val state = MutableLiveData<SetsViewModelState>()
    val selectedItem = MutableLiveData<Int>().apply {value = 0}


    private var sets: List<CardSet>? = null
    private var currentSetIndex = 0
    private var currentSet = ""
    private var currentType = ""
    private var isRefresh = true

    private val data = MutableLiveData<MutableList<ListItem>>().apply {value = mutableListOf()}
    private var page = 1

    private val searchdata = MutableLiveData<MutableList<ListItem>>().apply {value = mutableListOf()}
    private var searchFilter: String = ""
    private var searchPage = 1

    fun getViewState() = state
    fun clearViewState(){state.value = null}
    fun getData() = data
    fun getSearchData() = searchdata
    fun getSearchFilter() = searchFilter



    init {
        data.value?.let {
            if(it.size <= 0){
                loadCachedCards()
            }
        }
    }

    fun search(filter: String){
        if(filter.isNotEmpty()){
            searchFilter = filter
            searchPage = 1
            searchCards()
        }else{
            clearSearch()
        }
    }

    fun clearSearch(){
        searchdata.value = mutableListOf()
        searchFilter = ""
        searchPage = 1
        refreshData()
    }

    fun refreshData(){
        isRefresh = true
        if(searchFilter.isEmpty()){
            page = 1
            currentSetIndex = 0
            currentSet = ""
            currentType = ""
            getCards()
        }else{
            searchPage = 1
            searchCards()
        }
    }

    fun loadMore(){
        if(searchFilter.isEmpty()){
            getCards()
        }else{
            searchCards()
        }
    }



    private fun loadCachedCards() = CoroutineScope(dispatchers.io()).launch {
        try {
            repository.getCachedCards().let { list ->
                if(list.isNotEmpty()){
                    setData(groupItems(list))
                }
            }
        } catch (e: Exception) {
        }
        setCacheLoaded()
    }


    @Throws(Exception::class)
    private suspend fun getSets(){
        repository.getSets().let { response ->
            when(response.code){

                HttpURLConnection.HTTP_OK ->{
                    setCardsSets(response.sets)
                }

            }
        }
        if(sets.isNullOrEmpty()){
            throw java.lang.Exception("Sets not loaded")
        }
    }


    private fun getCards() = CoroutineScope(dispatchers.io()).launch {
        try {
            if(sets.isNullOrEmpty()){
                getSets()
            }
            sets?.get(currentSetIndex)?.let { set ->
                repository.getCards(page, set.code, isRefresh).let { response ->
                    when(response.code){

                        HttpURLConnection.HTTP_OK ->{
                            if(response.cards.isEmpty()){
                                loadNewSet()
                                return@launch
                            }
                            response.cards.let { list ->
                                if(isRefresh){
                                    setBackgroundImage(list)
                                    setData(groupItems(list))
                                }else{
                                    addData(groupItems(list))
                                }
                                isRefresh = false
                                page++
                            }
                        }

                        else -> setError(R.string.generic_network_error)
                    }
                }
            }
        } catch (e: Exception) {
            setError(R.string.generic_network_error)
        }
    }


    private fun searchCards() = CoroutineScope(dispatchers.io()).launch {
        try {
            repository.searchCards(searchPage, searchFilter).let { response ->
                when(response.code){

                    HttpURLConnection.HTTP_OK ->{
                        response.cards.let { list->
                            if(isRefresh){
                                setSearchData(list)
                            }else{
                                addSearchData(list)
                            }
                            isRefresh = false
                            searchPage++
                        }
                    }

                    else -> setError(R.string.generic_network_error)
                }
            }
        } catch (e: Exception) {
            setError(R.string.generic_network_error)
        }
    }


    private fun groupItems(list: List<ListItem>): List<ListItem>{
        return mutableListOf<ListItem>().apply {
            for (item in list){
                if(item is Card){
                    if(item.set != currentSet){
                        currentSet = item.set
                        this.add(CardSet(currentSet, item.setName))
                    }
                    if(item.type != currentType){
                        currentType = item.type
                        this.add(CardType(currentType, item.types))
                    }
                    this.add(item)
                }
            }
        }
    }


    private fun loadNewSet() {
        currentSetIndex++
        page = 1
        getCards()
    }


    private suspend fun setCardsSets(items: List<CardSet>) = withContext(dispatchers.main()) {
        sets = items
    }

    private suspend fun setData(items: List<ListItem>) = withContext(dispatchers.main()) {
        data.value = items.toMutableList()
    }

    private suspend fun setSearchData(items: List<ListItem>) = withContext(dispatchers.main()) {
        searchdata.value = items.toMutableList()
    }

    private suspend fun addData(items: List<ListItem>) = withContext(dispatchers.main()) {
        data.value?.addAll(items)
        state.value =
            SetsViewModelState.AddData(
                items
            )
    }

    private suspend fun addSearchData(items: List<ListItem>) = withContext(dispatchers.main()) {
        searchdata.value?.addAll(items)
        state.value =
            SetsViewModelState.AddData(
                items
            )
    }

    private suspend fun setError(@StringRes message: Int) = withContext(dispatchers.main()) {
        state.value =
            SetsViewModelState.Error(
                message
            )
    }

    private suspend fun setBackgroundImage(items: List<Card>){
        var tryCount = 0
        do{
            tryCount++
            val imageUrl = items[items.indices.random()].imageUrl
            if(imageUrl.isNotEmpty()){

                withContext(dispatchers.main()) {
                    state.value =
                        SetsViewModelState.BackgroundImage(
                            imageUrl
                        )
                }

            }
            if(tryCount >= 50){
                break
            }
        }while (imageUrl.isEmpty())
    }

    private suspend fun setCacheLoaded() = withContext(dispatchers.main()) {
        state.value =
            SetsViewModelState.CacheLoaded
    }

}