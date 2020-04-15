package com.bootcamp.bootcampmagic.utils

import com.bootcamp.bootcampmagic.repositories.MtgDataSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInitializer {

    fun getMtgDataSource(url: String): MtgDataSource {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MtgDataSource::class.java)
    }

}