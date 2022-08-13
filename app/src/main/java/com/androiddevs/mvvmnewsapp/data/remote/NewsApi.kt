package com.androiddevs.mvvmnewsapp.data.remote

import com.androiddevs.mvvmnewsapp.BuildConfig
import com.androiddevs.mvvmnewsapp.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "us",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>

    @GET("/v2/top-headlines")
    suspend fun getSearchedNews(
        @Query("q") searchQuery: String = "us",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>
}