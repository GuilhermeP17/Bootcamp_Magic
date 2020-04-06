package com.bootcamp.bootcampmagic.utils

import android.app.Application
import androidx.room.Room
import com.bootcamp.bootcampmagic.BuildConfig
import com.bootcamp.bootcampmagic.repositories.CardsDao
import com.bootcamp.bootcampmagic.repositories.CardsDataSource
import com.bootcamp.bootcampmagic.repositories.CardsDatabase

class App : Application() {

    companion object {
        private lateinit var cardsDatabase: CardsDatabase
        private lateinit var cardsDataSource: CardsDataSource
    }

    override fun onCreate() {
        super.onCreate()

        cardsDatabase = Room.databaseBuilder(
            this,
            CardsDatabase::class.java,
            CardsDatabase.DATABASE_NAME
        )
            .build()

        cardsDataSource = RetrofitInitializer.getCardsDataSource(BuildConfig.API_BASE_URL)
    }


    fun getCardsDao(): CardsDao = cardsDatabase.getCardsDao()
    fun getCardsDataSource(): CardsDataSource = cardsDataSource

}