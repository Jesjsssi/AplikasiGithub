package com.dicoding.restaurantreview.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.restaurantreview.database.Favorite
import com.dicoding.restaurantreview.database.FavoriteDao
import com.dicoding.restaurantreview.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository (application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun insert(favorite : Favorite) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }

    fun delete(favorite: Favorite) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }

    fun getAllFavoriteUsers(): LiveData<List<Favorite>> = mFavoriteDao.getAllFavoriteUser()

    fun checkFavorite(username: String): LiveData<Boolean> {
        return mFavoriteDao.checkFavorite(username)
    }
}