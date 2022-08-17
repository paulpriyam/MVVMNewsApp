package com.androiddevs.mvvmnewsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.androiddevs.mvvmnewsapp.model.Article
import com.androiddevs.mvvmnewsapp.repository.NewsRepository

class BreakingNewsPagingSource(private val repository: NewsRepository, private val countryCode: String) :
    PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val position = params.key ?: 1
            val result =
                repository.getBreakingNews(countryCode = countryCode, pageNumber = position)
            LoadResult.Page(
                data = result.body()?.articles ?: arrayListOf(),
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (result.body()?.articles?.isEmpty() == true) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}