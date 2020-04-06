package com.bootcamp.bootcampmagic.repositories

import com.bootcamp.bootcampmagic.models.CardsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CardsDataSource {

    @GET("v1/cards?orderBy=set,type,name")
    fun getCards(@Query("page") page: Int): Call<CardsResponse>

    @GET("v1/cards?orderBy=name")
    fun getCards(@Query("page") page: Int, @Query("name") name: String): Call<CardsResponse>

}