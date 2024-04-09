package com.dicoding.restaurantreview.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.restaurantreview.repository.FavoriteRepository
import com.dicoding.restaurantreview.repository.FavoriteUpdateViewModel
import com.dicoding.restaurantreview.ui.detail.DetailViewModel
import com.dicoding.restaurantreview.ui.favorite.FavoriteViewModel

class FavoriteModelFactory(private val mApplication: Application) : ViewModelProvider.Factory {
    private val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepository(mApplication)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavoriteUpdateViewModel::class.java)) {
            return FavoriteUpdateViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(favoriteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteModelFactory? = null

        fun getInstance(application: Application): FavoriteModelFactory {
            if (INSTANCE == null) {
                synchronized(FavoriteModelFactory::class.java) {
                    INSTANCE = FavoriteModelFactory(application)
                }
            }
            return INSTANCE as FavoriteModelFactory
        }
    }
}