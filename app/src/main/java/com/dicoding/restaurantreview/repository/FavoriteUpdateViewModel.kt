package com.dicoding.restaurantreview.repository

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dicoding.restaurantreview.database.Favorite

class FavoriteUpdateViewModel (application: Application) : AndroidViewModel(application) {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }
}