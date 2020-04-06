package com.bootcamp.bootcampmagic.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bootcamp.bootcampmagic.models.Card
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardsDatabaseTest {

    private lateinit var database: CardsDatabase
    private lateinit var cardsDao: CardsDao


    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            CardsDatabase::class.java)
            .build()
        cardsDao = database.getCardsDao()
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun shouldWriteAndReadCards(){
        val cardsList = createCardsList()

        cardsDao.updateData(cardsList)
        val dbCardsList = cardsDao.getAll()

        assertTrue(cardsList.isNotEmpty())
        assertTrue(dbCardsList.size == cardsList.size)
    }


    private fun createCardsList(): ArrayList<Card>{
        return ArrayList<Card>().apply {
            for (index in 1..10){
                add(Card(index.toLong(), "name $index", "type $index","set $index","setNAme $index","imageUrl $index"))
            }
        }
    }

}