package com.dicoding.restaurantreview.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.restaurantreview.database.Favorite
import com.dicoding.restaurantreview.repository.FavoriteRepository

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel()  {
    fun getAllFavoriteUsers(): LiveData<List<Favorite>> {
        return repository.getAllFavoriteUsers()
    }
}