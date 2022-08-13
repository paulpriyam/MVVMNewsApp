package com.androiddevs.mvvmnewsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.utils.converters.SourceConverter
import com.androiddevs.mvvmnewsapp.data.local.dao.NewsDao
import com.androiddevs.mvvmnewsapp.model.Article


@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(SourceConverter::class)
abstract class NewsDatabase :RoomDatabase(){

    abstract fun getNewsDao(): NewsDao
}