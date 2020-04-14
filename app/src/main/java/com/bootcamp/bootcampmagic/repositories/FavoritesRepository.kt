package com.bootcamp.bootcampmagic.repositories

import com.bootcamp.bootcampmagic.models.Favorite
import com.bootcamp.bootcampmagic.utils.DefaultDispatcherProvider
import com.bootcamp.bootcampmagic.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class FavoritesRepository(
    private val database: FavoritesDao,
    private val dispatcher: DispatcherProvider = DefaultDispatcherProvider()
) {

    suspend fun getFavorites() : List<Favorite> {
        return withContext(dispatcher.io()){
            database.getAll()
        }
    }

}