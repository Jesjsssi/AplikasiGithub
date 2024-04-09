package com.dicoding.restaurantreview.ui.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.adapter.FavoriteAdapter
import com.dicoding.restaurantreview.databinding.ActivityFavoriteListBinding
import com.dicoding.restaurantreview.helper.FavoriteModelFactory

class FavoriteList : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteListBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = ""

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        favoriteAdapter = FavoriteAdapter(this)
        binding.rvFavorite.adapter = favoriteAdapter

        val factory = FavoriteModelFactory.getInstance(application)
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        favoriteViewModel.getAllFavoriteUsers().observe(this) { favorite ->
            favoriteAdapter.submitList(favorite)
        }
    }
}