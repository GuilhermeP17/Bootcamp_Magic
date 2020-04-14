package com.bootcamp.bootcampmagic.viewmodels.favorites

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
import com.bootcamp.bootcampmagic.utils.SharedViewModel
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModelState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    private val repository: MtgRepository,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
): ViewModel(), SharedViewModel {

    private val state = MutableLiveData<FavoritesViewModelState>()
    private var selectedItem: Int = -1
    private var currentSet = ""
    private var currentType = ""

    private val data = MutableLiveData<MutableList<ListItem>>().apply {value = mutableListOf()}
    private val searchdata = MutableLiveData<MutableList<ListItem>>().apply {value = mutableListOf()}
    private var searchFilter: String = ""

    override fun getSetsViewModelState() : MutableLiveData<SetsViewModelState>? = null
    override fun getFavoritesViewModelState() = state
    override fun clearViewState(){state.value = null}
    override fun getData() = data
    override fun getSelectedItem() = selectedItem
    override fun setSelectedItem(value: Int) {
        selectedItem = value
    }
    fun getSearchData() = searchdata
    fun getSearchFilter() = searchFilter



    fun search(filter: String){
        if(filter.isNotEmpty()){
            searchFilter = filter
            searchFavorites()
        }else{
            clearSearch()
        }
    }

    fun clearSearch(){
        searchdata.value = mutableListOf()
        searchFilter = ""
        //refreshData()
        data.value = data.value
    }

    fun refreshData(){
        if(searchFilter.isEmpty()){
            currentSet = ""
            currentType = ""
            getFavorites()
        }else{
            clearSearch()
        }
    }

    override fun loadMore(){}

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


    private fun getFavorites() = CoroutineScope(dispatchers.io()).launch {
        try {
            setData(groupItems(repository.getFavorites()))
        } catch (e: Exception) {
            setError(R.string.generic_network_error)
        }
    }


    private fun searchFavorites() = CoroutineScope(dispatchers.io()).launch {
        try {
            setSearchData(groupItems(repository.searchFavorites(searchFilter)))
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

    private suspend fun setData(items: List<ListItem>) = withContext(dispatchers.main()) {
        data.value = items.toMutableList()
    }

    private suspend fun setSearchData(items: List<ListItem>) = withContext(dispatchers.main()) {
        searchdata.value = items.toMutableList()
    }

    private suspend fun setError(@StringRes message: Int) = withContext(dispatchers.main()) {
        state.value =
            FavoritesViewModelState.Error(
                message
            )
    }

}