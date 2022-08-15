package com.androiddevs.mvvmnewsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.model.Article
import com.androiddevs.mvvmnewsapp.model.NewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.utils.converters.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(val repository: NewsRepository) : ViewModel() {

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
        _breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(countryCode, pageNumber)
        _breakingNews.postValue(handleBreakingNewsResponse(response))
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
        _searchNews.postValue(Resource.Loading())
        val response = repository.getSearchedNews(searchQuery, searchPageNumber)
        _searchNews.postValue(handleSearchedNewsResponse(response))
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

}