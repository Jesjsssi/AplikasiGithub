package com.dicoding.restaurantreview.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val id: Int?,
    val login: String?,
    val html_url: String?,
    val avatar_url: String?,
): Parcelable
