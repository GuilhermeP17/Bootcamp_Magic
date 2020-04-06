package com.bootcamp.bootcampmagic.repositories

import androidx.room.*
import com.bootcamp.bootcampmagic.models.Card

@Database(entities = [Card::class], version = 1, exportSchema = false)
abstract class CardsDatabase : RoomDatabase() {
    companion object{
        val DATABASE_NAME = "Card"
    }
    abstract fun getCardsDao(): CardsDao
}

@Dao
interface CardsDao{

    @Query("SELECT * FROM Card")
    fun getAll(): List<Card>

    @Transaction
    fun updateData(cards: List<Card>) {
        deleteAll()
        insertAll(cards)
    }

    @Insert
    fun insertAll(cards: List<Card>)

    @Query("DELETE FROM Card")
    fun deleteAll()

}