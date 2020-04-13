package com.bootcamp.bootcampmagic.utils

import android.app.Application
import androidx.room.Room
import com.bootcamp.bootcampmagic.BuildConfig
import com.bootcamp.bootcampmagic.repositories.CardsDao
import com.bootcamp.bootcampmagic.repositories.CardsDatabase
import com.bootcamp.bootcampmagic.repositories.MtgDataSource

class App: Application() {
    companion object{
        private lateinit var cardsDatabase: CardsDao
        private lateinit var mtgDataSource: MtgDataSource
    }

    override fun onCreate() {
        super.onCreate()

        cardsDatabase = Room.databaseBuilder(
            this,
            CardsDatabase::class.java,
            CardsDatabase.DATABASE_NAME)
            .build()
            .getCardsDao()

        mtgDataSource = RetrofitInitializer.getMtgDataSource(BuildConfig.API_BASE_URL)
    }


    fun getCardsDao(): CardsDao = cardsDatabase
    fun getMtgDataSource(): MtgDataSource = mtgDataSource

}