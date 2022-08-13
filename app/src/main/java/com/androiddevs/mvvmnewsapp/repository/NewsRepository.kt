package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.data.local.dao.NewsDao
import com.androiddevs.mvvmnewsapp.data.remote.NewsApi
import javax.inject.Inject

class NewsRepository @Inject constructor(
    val newsDao:NewsDao,
    val api:NewsApi
) {
}