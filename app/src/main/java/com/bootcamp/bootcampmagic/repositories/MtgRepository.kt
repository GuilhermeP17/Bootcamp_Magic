package com.bootcamp.bootcampmagic.repositories

import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.models.CardsResponse
import com.bootcamp.bootcampmagic.models.SetsResponse
import com.bootcamp.bootcampmagic.utils.DefaultDispatcherProvider
import com.bootcamp.bootcampmagic.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

class MtgRepository(
    private val cardsDatabase: CardsDao,
    private val dataSource: MtgDataSource,
    private val dispatcher: DispatcherProvider = DefaultDispatcherProvider()
) {

    suspend fun getCachedCards(): List<Card> = withContext(dispatcher.io()) {
        cardsDatabase.getCachedCards()
    }

    suspend fun getSets(): SetsResponse = withContext(dispatcher.io()) {
        SetsResponse(mutableListOf()).apply {
            dataSource.getSets().execute().let { response ->
                this.code = response.code()
                if(response.code() == HttpURLConnection.HTTP_OK){
                    response.body()?.let { body ->
                        this.sets = body.sets
                    }
                }
            }
        }
    }

    suspend fun getCards(page: Int, set: String, saveCache: Boolean): CardsResponse = withContext(dispatcher.io()) {
        CardsResponse(mutableListOf()).apply {
            dataSource.getCards(page, set).execute().let { response ->
                this.code = response.code()
                if(response.code() == HttpURLConnection.HTTP_OK){
                    response.body()?.let { body ->
                        this.cards = body.cards

                        for(card in this.cards){
                            if(card.imageUrl.isNullOrEmpty()){
                                card.imageUrl = ""
                            }
                            if(card.types.isNotEmpty()){
                                card.type = card.types[0]
                            }

                            if(cardsDatabase.getFavoriteCard(card.id) != null){
                                card.favorite = true
                            }
                            if(saveCache){
                                card.isCache = true
                            }
                        }

                        if(saveCache){
                            cardsDatabase.updateCache(body.cards)
                        }
                    }
                }
            }
        }
    }

    suspend fun searchCards(page: Int, name: String): CardsResponse = withContext(dispatcher.io()) {
        CardsResponse(mutableListOf()).apply {
            dataSource.searchCards(page, name).execute().let { response ->
                this.code = response.code()
                if(response.code() == HttpURLConnection.HTTP_OK){
                    response.body()?.let { body ->
                        this.cards = body.cards

                        for(card in this.cards){
                            if(card.imageUrl.isNullOrEmpty()){
                                card.imageUrl = ""
                            }
                            if(card.types.isNotEmpty()){
                                card.type = card.types[0]
                            }

                            if(cardsDatabase.getFavoriteCard(card.id) != null){
                                card.favorite = true
                            }
                        }
                    }
                }
            }
        }
    }



    suspend fun getFavorites(): List<Card> = withContext(dispatcher.io()) {
        cardsDatabase.getFavorites()
    }

    suspend fun searchFavorites(name: String): List<Card> = withContext(dispatcher.io()) {
        cardsDatabase.searchFavorites(name)
    }

    suspend fun addFavorite(card: Card) = withContext(dispatcher.io()) {
        card.favorite = true
        cardsDatabase.addFavorite(card)
    }

    suspend fun removeFavorite(card: Card) = withContext(dispatcher.io()) {
        card.favorite = false
        cardsDatabase.removeFavorite(card.id)
    }
}