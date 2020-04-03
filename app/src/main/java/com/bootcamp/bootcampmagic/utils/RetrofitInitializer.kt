package com.bootcamp.bootcampmagic.utils

import com.bootcamp.bootcampmagic.BuildConfig
import com.bootcamp.bootcampmagic.repositories.CardsDataSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInitializer {

    fun getCardsDataSource(): CardsDataSource {
        return getCardsDataSource(BuildConfig.API_BASE_URL)
    }
    fun getCardsDataSource(url: String): CardsDataSource {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CardsDataSource::class.java)
    }

}