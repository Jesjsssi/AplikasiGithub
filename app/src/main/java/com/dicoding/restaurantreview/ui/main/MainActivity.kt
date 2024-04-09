package com.dicoding.restaurantreview.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.restaurantreview.R
import com.dicoding.restaurantreview.data.response.ItemsItem
import com.dicoding.restaurantreview.databinding.ActivityMainBinding
import com.dicoding.restaurantreview.ui.detail.DetailActivity
import com.dicoding.restaurantreview.ui.favorite.FavoriteList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private var queryProfileSearch: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findRestaurant()
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        supportActionBar?.hide()

        mainViewModel.listReview.observe(this) { githubprofile ->
            if (githubprofile != null && githubprofile.isNotEmpty()) {
                setReviewData(githubprofile)
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        if (savedInstanceState == null) {
            mainViewModel.findProfile()
        } else {
            queryProfileSearch = savedInstanceState.getString("search_query")
            queryProfileSearch?.let { query ->
                searchProfileGithub(query)
            }
        }
        setSupportActionBar(binding.searchBar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                startActivity(Intent(this@MainActivity, FavoriteList::class.java))
                true
            }
            R.id.setting -> {
                startActivity(Intent(this@MainActivity, SettingMode::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun findRestaurant() {
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            binding.searchBar.setText(binding.searchView.text)
            binding.searchView.hide()
            val query = binding.searchBar.text.toString()
            if (query.isNotEmpty()) {
                searchProfileGithub(query)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Please input Github Username",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.searchView.hide()
            return@setOnEditorActionListener false
        }
    }

    private fun searchProfileGithub(query: String) {
        queryProfileSearch = query
        mainViewModel.searchProfileGithub(query)
    }

    private fun setReviewData(items: List<ItemsItem>) {
        val adapter = ReviewAdapter(object : ReviewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem, position: Int) {
                showUserDetail(data.login)
            }
        })
        adapter.submitList(items)
        binding.rvReview.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUserDetail(username: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }
}