package com.androiddevs.mvvmnewsapp.utils.converters

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorMessage: String, data: T?) : Resource<T>(data, errorMessage)
    class Loading<T> : Resource<T>()
}