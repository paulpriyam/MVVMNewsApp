package com.androiddevs.mvvmnewsapp.repository

import com.androiddevs.mvvmnewsapp.data.local.dao.NewsDao
import com.androiddevs.mvvmnewsapp.data.remote.NewsApi
import com.androiddevs.mvvmnewsapp.model.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    val newsDao: NewsDao,
    val api: NewsApi
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchedNews(searchQuery: String, pageNumber: Int) =
        api.getSearchedNews(searchQuery, pageNumber)

    suspend fun addToFavourite(article: Article) = newsDao.updateInsert(article)

    suspend fun deleteFromFavourite(article: Article) = newsDao.deleteArticle(article)

    fun getAllFavouriteNews() = newsDao.getAllArticles()
}