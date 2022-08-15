package com.androiddevs.mvvmnewsapp.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.androiddevs.mvvmnewsapp.NewsApplication
import com.androiddevs.mvvmnewsapp.model.Article
import com.androiddevs.mvvmnewsapp.model.NewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.utils.converters.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(app: Application, val repository: NewsRepository) :
    AndroidViewModel(app) {

    private val _breakingNews: MutableLiveData<Resource<NewsResponse>> =
        MutableLiveData<Resource<NewsResponse>>()
    val breakingNews: LiveData<Resource<NewsResponse>> = _breakingNews
    private val pageNumber = 1

    private val _searchNews: MutableLiveData<Resource<NewsResponse>> =
        MutableLiveData<Resource<NewsResponse>>()
    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews
    private val searchPageNumber = 1

    private var _favouriteNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val favouriteNews: LiveData<Resource<NewsResponse>> = _favouriteNews

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        try {
            if (checkNetworkConnection()) {
                _breakingNews.postValue(Resource.Loading())
                val response = repository.getBreakingNews(countryCode, pageNumber)
                _breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                _breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _breakingNews.postValue(Resource.Error("Fatal Error"))
                else -> _breakingNews.postValue(Resource.Error("Unknown Error"))
            }
        }

    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    fun getSearchedNews(searchQuery: String) = viewModelScope.launch {
       try {
           if(checkNetworkConnection()){
               _searchNews.postValue(Resource.Loading())
               val response = repository.getSearchedNews(searchQuery, searchPageNumber)
               _searchNews.postValue(handleSearchedNewsResponse(response))
           }else{
               _breakingNews.postValue(Resource.Error("No Internet Connection"))
           }
       }catch (t:Throwable){
           when (t) {
               is IOException -> _breakingNews.postValue(Resource.Error("Fatal Error"))
               else -> _breakingNews.postValue(Resource.Error("Unknown Error"))
           }
       }
    }

    private fun handleSearchedNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    fun addToFavourite(article: Article) =
        viewModelScope.launch { repository.addToFavourite(article) }

    fun deleteFromFavourite(article: Article) = viewModelScope.launch {
        repository.deleteFromFavourite(article)
    }

    fun getAllFavouriteNews() = repository.getAllFavouriteNews()

    private fun checkNetworkConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.apply {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}