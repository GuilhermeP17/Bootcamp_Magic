package com.bootcamp.bootcampmagic.repositories

import androidx.room.*
import com.bootcamp.bootcampmagic.models.Favorite

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FavoritesDatabase : RoomDatabase(){
    companion object{
        val DATABASE_NAME = "Favorites"
    }

    abstract fun getCardsDao(): FavoritesDao
}

@Dao
interface FavoritesDao{

    @Query("SELECT * FROM Favorite")
    fun getAll(): List<Favorite>

    @Transaction
    fun updateData(cards: List<Favorite>) {
        deleteAll()
        insertAll(cards)
    }

    @Insert
    fun insertAll(cards: List<Favorite>)

    @Query("DELETE FROM Favorite")
    fun deleteAll()

}