package com.dicoding.restaurantreview.data.retrofit

import com.dicoding.restaurantreview.data.response.DetailUserResponse
import com.dicoding.restaurantreview.data.response.ItemsItem
import com.dicoding.restaurantreview.data.response.RestaurantResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getRestaurant(
        @Query("q") query: String
    ): Call<RestaurantResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}