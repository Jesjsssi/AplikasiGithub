package com.dicoding.restaurantreview.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.restaurantreview.R
import com.dicoding.restaurantreview.adapter.SectionsPagerAdapter
import com.dicoding.restaurantreview.data.response.DetailUserResponse
import com.dicoding.restaurantreview.database.Favorite
import com.dicoding.restaurantreview.databinding.ActivityDetailBinding
import com.dicoding.restaurantreview.helper.FavoriteModelFactory
import com.dicoding.restaurantreview.repository.FavoriteUpdateViewModel
import com.dicoding.restaurantreview.ui.favorite.FavoriteViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favoriteUpdateViewModel: FavoriteUpdateViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var isFavoriteLiveData: LiveData<Boolean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")

        if (username.isNullOrEmpty()) {
            return
        }

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        detailViewModel.getDetailUser(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        sectionsPagerAdapter.username = username
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()

        supportActionBar?.elevation = 0f

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailViewModel.detailUserGithub.observe(this) { userData ->
            setDetailUser(userData)
        }

        val favoriteViewModelFactory = FavoriteModelFactory(application)

        favoriteViewModel = ViewModelProvider(this, favoriteViewModelFactory)[FavoriteViewModel::class.java]
        favoriteUpdateViewModel = ViewModelProvider(this, favoriteViewModelFactory)[FavoriteUpdateViewModel::class.java]

        checkFavorite(username)

        binding.fav.setOnClickListener {
            this.checkFavorite(username)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailUser(detailUser: DetailUserResponse) {
        binding.tvName.text = detailUser.name
        binding.tvUser.text = detailUser.login
        binding.tvFollowers.text = "${detailUser.followers} Followers"
        binding.tvFollowing.text = "${detailUser.following} Following"
        Glide.with(this)
            .load(detailUser.avatarUrl)
            .into(binding.tvProfile)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkFavorite(username: String) {
        isFavoriteLiveData?.removeObservers(this)
        isFavoriteLiveData = detailViewModel.checkFavorite(username)
        isFavoriteLiveData?.observe(this) { isFavorite ->
            if (isFavorite) {
                binding.fav.setImageResource(R.drawable.ic_favfull)
            } else {
                binding.fav.setImageResource(R.drawable.ic_favorite)
            }

            binding.fav.setOnClickListener {
                if (isFavorite) {
                    deleteFavorite(username)
                } else {
                    saveFavorite()
                }
            }
        }
    }

    private fun saveFavorite() {
        val detailUser = detailViewModel.detailUserGithub.value ?: return
        val favorite = Favorite(username = detailUser.login, avatarUrl = detailUser.avatarUrl)
        favoriteUpdateViewModel.insert(favorite)

        Toast.makeText(this, "Menambahkan user ke favorite", Toast.LENGTH_SHORT).show()
    }

    private fun deleteFavorite(username: String) {
        val detailUser = detailViewModel.detailUserGithub.value ?: return
        val favorite = Favorite(username = username, avatarUrl = detailUser.avatarUrl)
        favoriteUpdateViewModel.delete(favorite)

        Toast.makeText(this, "Menghapus user dari favorite", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLE = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}