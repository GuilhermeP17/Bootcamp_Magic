package com.bootcamp.bootcampmagic.viewmodels.sets

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.models.*
import com.bootcamp.bootcampmagic.repositories.MtgRepository
import com.bootcamp.bootcampmagic.utils.DefaultDispatcherProvider
import com.bootcamp.bootcampmagic.utils.DispatcherProvider
import com.bootcamp.bootcampmagic.utils.SharedViewModel
import com.bootcamp.bootcampmagic.viewmodels.favorites.FavoritesViewModelState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection


class SetsViewModel (
    private val repository: MtgRepository,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
): ViewModel(), SharedViewModel {

    private val state = MutableLiveData<SetsViewModelState>()
    private var sets: List<CardSet>? = null
    private val mainData: MutableList<ListItem> = mutableListOf()
    private val searchData: MutableList<ListItem> = mutableListOf()

    private val data = MutableLiveData<MutableList<ListItem>>().apply {value = mutableListOf()}
    private var currentSetIndex = 0
    private var currentSet = ""
    private var currentType = ""
    private var page = 1
    private var searchPage = 1
    private var selectedItem: Int = -1
    private var searchFilter: String = ""

    override fun getSetsViewModelState() = state
    override fun getFavoritesViewModelState(): MutableLiveData<FavoritesViewModelState>? = null
    override fun clearViewState(){state.value = null}
    override fun getData() = data
    override fun getSelectedItem() = selectedItem
    override fun setSelectedItem(value: Int) {
        selectedItem = value
    }


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
            if(searchFilter.isNotEmpty()){
                searchData.clear()
                searchFilter = ""
                searchPage = 1
                CoroutineScope(dispatchers.main()).launch {
                    data.value = mainData
                    setLoadingType(LoadingType.LOADED)
                }
            }
        }
    }

    fun refreshData(){
        if(searchFilter.isEmpty()){
            page = 1
            currentSetIndex = 0
            currentSet = ""
            currentType = ""
            mainData.clear()
            getCards()
        }else{
            searchPage = 1
            searchData.clear()
            searchCards()
        }
    }


    override fun loadMore(){
        if(searchFilter.isEmpty()){
            getCards()
        }else{
            searchCards()
        }
    }

    override fun setFavorite(position: Int, favorite: Boolean){
        val card = (data.value?.get(position) as Card)
        card.favorite = favorite

        CoroutineScope(dispatchers.io()).launch {
            if(favorite){
                repository.addFavorite(card)
            }else{
                repository.removeFavorite(card)
            }
        }
    }



    private fun loadCachedCards() = CoroutineScope(dispatchers.io()).launch {
        /*setLoadingType(LoadingType.LOADING)
        try {
            repository.getCachedCards().let { list ->
                if(list.isNotEmpty()){
                    setData(groupItems(list))
                    setLoadingType(LoadingType.LOADED)
                }
            }
        } catch (e: Exception) {
        }
        currentSet = ""
        currentType = ""*/
        getCards()
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
        var isLoading = true
        data.value?.let {
            if(it.isNotEmpty()){
                isLoading = false
            }
        }
        if(isLoading){
            setLoadingType(LoadingType.LOADING)
        }
        try {
            if(sets.isNullOrEmpty()){
                getSets()
            }
            sets?.get(currentSetIndex)?.let { set ->
                var saveCache = false
                if(page <= 1){
                    if(currentSetIndex <= 0){
                        saveCache = true
                    }
                }

                repository.getCards(page, set.code, saveCache).let { response ->
                    when(response.code){

                        HttpURLConnection.HTTP_OK ->{
                            if(response.cards.isEmpty()){
                                loadNewSet()
                                return@launch
                            }
                            response.cards.let { list ->

                                val groupedItems = groupItems(list)
                                if(mainData.isEmpty()){
                                    mainData.addAll(groupedItems)
                                    setData(mainData)
                                    setBackgroundImage(list)
                                }else{
                                    mainData.addAll(groupedItems)
                                    addData(groupedItems)
                                }
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
        if(mainData.isEmpty()){
            setLoadingType(LoadingType.NO_CONTENT)
        }else{
            setLoadingType(LoadingType.LOADED)
        }
    }


    private fun searchCards() = CoroutineScope(dispatchers.io()).launch {
        setLoadingType(LoadingType.LOADING)
        try {
            repository.searchCards(searchPage, searchFilter).let { response ->
                when(response.code){

                    HttpURLConnection.HTTP_OK ->{
                        response.cards.let { list->
                            if(searchData.isEmpty()){
                                searchData.addAll(list)
                                setData(searchData)
                            }else{
                                searchData.addAll(list)
                                addData(list)
                            }
                            searchPage++
                        }
                    }

                    else -> setError(R.string.generic_network_error)
                }
            }
        } catch (e: Exception) {
            setError(R.string.generic_network_error)
        }
        if(searchData.isEmpty()){
            setLoadingType(LoadingType.NO_CONTENT)
        }else{
            setLoadingType(LoadingType.LOADED)
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

    private suspend fun addData(items: List<ListItem>) = withContext(dispatchers.main()) {
        state.value =SetsViewModelState.AddData(items)
    }

    private suspend fun setError(@StringRes message: Int) = withContext(dispatchers.main()) {
        state.value =
            SetsViewModelState.Error(
                message
            )
    }

    private suspend fun setLoadingType(type: LoadingType) = withContext(dispatchers.main()) {
        state.value =
            SetsViewModelState.LoadingState(
                type
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

}