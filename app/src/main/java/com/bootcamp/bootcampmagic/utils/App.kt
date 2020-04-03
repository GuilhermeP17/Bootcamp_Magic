package com.bootcamp.bootcampmagic.utils

import android.app.Application
import androidx.room.Room
import com.bootcamp.bootcampmagic.repositories.CardsDao
import com.bootcamp.bootcampmagic.repositories.CardsDatabase

class App: Application() {

    private lateinit var cardsDatabase: CardsDatabase

    override fun onCreate() {
        super.onCreate()

        cardsDatabase = Room.databaseBuilder(
            this,
            CardsDatabase::class.java,
            CardsDatabase.DATABASE_NAME)
            .build()
    }


    fun getCardsDao(): CardsDao{
        return cardsDatabase.getCardsDao()
    }

}