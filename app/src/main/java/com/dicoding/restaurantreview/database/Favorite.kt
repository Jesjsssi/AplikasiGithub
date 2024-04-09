package com.dicoding.restaurantreview.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Favorite")
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username: String = " ",

    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null,

    ) : Parcelable

