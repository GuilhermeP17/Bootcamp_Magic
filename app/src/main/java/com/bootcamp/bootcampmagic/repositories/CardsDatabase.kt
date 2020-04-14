package com.bootcamp.bootcampmagic.repositories

import androidx.room.*
import com.bootcamp.bootcampmagic.models.Card

@Database(entities = [Card::class], version = 2, exportSchema = false)
abstract class CardsDatabase : RoomDatabase() {
    companion object{
        val DATABASE_NAME = "Card"
    }
    abstract fun getCardsDao(): CardsDao
}

@Dao
interface CardsDao{

    @Query("SELECT * FROM Card WHERE isCache = 1")
    fun getCachedCards(): List<Card>

    @Transaction
    fun updateCache(cards: List<Card>) {
        deleteCache()
        insertCache(cards)
    }

    @Query("DELETE FROM Card WHERE isCache LIKE 1 AND favorite LIKE 0")
    fun deleteCache()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCache(cards: List<Card>)

    @Query("SELECT * FROM Card WHERE favorite LIKE 1 ORDER BY setName, type")
    fun getFavorites(): List<Card>

    @Query("SELECT * FROM Card WHERE favorite LIKE 1 AND id LIKE :id ORDER BY name")
    fun getFavoriteCard(id: String): Card?

    @Query("SELECT * FROM Card WHERE favorite LIKE 1 AND name LIKE :name ORDER BY name")
    fun searchFavorites(name: String): List<Card>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(cards: Card)

    @Query("DELETE FROM Card WHERE favorite LIKE 1 AND id LIKE :id")
    fun removeFavorite(id: String)

}