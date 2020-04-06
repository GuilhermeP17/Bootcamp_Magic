package com.bootcamp.bootcampmagic.repositories

import com.bootcamp.bootcampmagic.models.CardsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

class CardsRepository(
    private val dataSource: CardsDataSource,
    private val databaseDao: CardsDao
){

    suspend fun getCache(): CardsResponse = withContext(Dispatchers.IO) {
        CardsResponse(ArrayList()).apply {
            this.cards = databaseDao.getAll()
        }
    }


    suspend fun getCards(page: Int): CardsResponse = withContext(Dispatchers.IO) {
        CardsResponse(ArrayList()).apply {

            val response = dataSource.getCards(page).execute()
            this.errorCode = response.code()
            when(response.code()){

                HttpURLConnection.HTTP_OK ->
                    response.body()?.let { body ->
                        if(body.cards.isNotEmpty()){
                            this.cards = body.cards

                            //Filter
                            for(card in this.cards){
                                if(card.imageUrl == null){
                                    card.imageUrl = "..."
                                }
                            }

                            if(page == 1){
                                databaseDao.updateData(body.cards)
                            }
                        }
                    }

            }
        }
    }

    suspend fun searchCards(page: Int, filter: String): CardsResponse = withContext(Dispatchers.IO)  {
        CardsResponse(ArrayList()).apply {

            val response = dataSource.getCards(page, filter).execute()
            this.errorCode = response.code()
            when(response.code()){

                HttpURLConnection.HTTP_OK ->
                    response.body()?.let { body ->
                        if(body.cards.isNotEmpty()){
                            this.cards = body.cards

                            //Filter
                            for(card in this.cards){
                                if(card.imageUrl == null){
                                    card.imageUrl = "..."
                                }
                            }
                        }
                    }

            }
        }
    }

}