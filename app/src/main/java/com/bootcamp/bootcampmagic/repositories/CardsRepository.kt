package com.bootcamp.bootcampmagic.repositories

import com.bootcamp.bootcampmagic.models.CardsResponse
import java.net.HttpURLConnection

class CardsRepository(
    private val dataSource: CardsDataSource,
    private val databaseDao: CardsDao
){

    fun getCards(page: Int): CardsResponse {
        val cardsResponse = CardsResponse(ArrayList())
        val response = dataSource.getCards(page).execute()
        cardsResponse.errorCode = response.code()
        when(response.code()){

            HttpURLConnection.HTTP_OK ->
                response.body()?.let { body ->
                    if(body.cards.isNotEmpty()){
                        cardsResponse.cards = body.cards
                        if(page == 1){
                            databaseDao.updateData(body.cards)
                        }
                    }
                }

        }
        if(page == 1){
            if(cardsResponse.cards.isEmpty()){
                cardsResponse.cards = databaseDao.getAll()
                cardsResponse.errorCode = HttpURLConnection.HTTP_OK
            }
        }

        return cardsResponse
    }

    fun searchCards(page: Int, filter: String): CardsResponse {
        val cardsResponse = CardsResponse(ArrayList())
        val response = dataSource.getCards(page, filter).execute()
        cardsResponse.errorCode = response.code()
        when(response.code()){

            HttpURLConnection.HTTP_OK ->
                response.body()?.let {
                    if(it.cards.isNotEmpty()){
                        cardsResponse.cards = it.cards
                    }
                }

        }

        return cardsResponse
    }

}