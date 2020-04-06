package com.bootcamp.bootcampmagic.utils

import com.bootcamp.bootcampmagic.repositories.CardsDataSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInitializer {

    fun getCardsDataSource(url: String): CardsDataSource {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CardsDataSource::class.java)
    }

}