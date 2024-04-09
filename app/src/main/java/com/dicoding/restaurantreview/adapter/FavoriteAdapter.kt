package com.dicoding.restaurantreview.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.restaurantreview.database.Favorite
import com.dicoding.restaurantreview.databinding.ItemReviewBinding
import com.dicoding.restaurantreview.ui.detail.DetailActivity

class FavoriteAdapter(private val context: Context) : ListAdapter<Favorite, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class FavoriteViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val favorite = getItem(position)
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("username", favorite.username)
                intent.putExtra("avatarUrl", favorite.avatarUrl)
                context.startActivity(intent)
            }
        }

        fun bind(favorite: Favorite) {
            binding.tvUsername.text = favorite.username

            Glide.with(binding.root)
                .load(favorite.avatarUrl)
                .into(binding.imageView)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Favorite>() {
            override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem == newItem
            }
        }
    }
}