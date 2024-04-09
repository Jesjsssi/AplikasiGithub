package com.dicoding.restaurantreview.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.restaurantreview.data.response.DetailUserResponse
import com.dicoding.restaurantreview.data.retrofit.ApiConfig
import com.dicoding.restaurantreview.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailViewModel(application: Application) : AndroidViewModel(application)  {
    private val _detailUserGithub = MutableLiveData<DetailUserResponse>()
    val detailUserGithub: LiveData<DetailUserResponse> = _detailUserGithub

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

   private val mFavoriteUserRepository: FavoriteRepository = FavoriteRepository(application)

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val detailUser: DetailUserResponse? = response.body()
                    detailUser?.let { user ->
                        _detailUserGithub.value = user
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun checkFavorite(username: String): LiveData<Boolean> {
        return mFavoriteUserRepository.checkFavorite(username)
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}


