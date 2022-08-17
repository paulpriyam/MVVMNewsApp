package com.androiddevs.mvvmnewsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.androiddevs.mvvmnewsapp.model.Article
import com.androiddevs.mvvmnewsapp.repository.NewsRepository

class SearchNewsPagingSource(val searchQuery: String, val repository: NewsRepository) :
    PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val currentPage = params.key ?: 1
            val result = repository.getSearchedNews(searchQuery, currentPage)
            LoadResult.Page(
                data = result.body()?.articles ?: arrayListOf(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (result.body()?.articles.isNullOrEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}