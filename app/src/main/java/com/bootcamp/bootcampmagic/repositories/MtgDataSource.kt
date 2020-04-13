package com.bootcamp.bootcampmagic.repositories

import com.bootcamp.bootcampmagic.models.CardsResponse
import com.bootcamp.bootcampmagic.models.SetsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MtgDataSource {

    @GET("v1/sets")
    fun getSets(): Call<SetsResponse>

    @GET("v1/cards?orderBy=types")
    fun getCards(@Query("page") page: Int, @Query("set") set: String): Call<CardsResponse>

    @GET("v1/cards?orderBy=name")
    fun searchCards(@Query("page") page: Int, @Query("name") name: String): Call<CardsResponse>

}