package com.androiddevs.mvvmnewsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RemoteKeys")
data class ArticleRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val prevkey: Int?,
    val nextKey: Int?
)
