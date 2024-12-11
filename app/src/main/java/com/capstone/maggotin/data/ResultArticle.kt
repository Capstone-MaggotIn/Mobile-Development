package com.capstone.maggotin.data

sealed class ResultArticle<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultArticle<T>()
    data class Error(val error: String) : ResultArticle<Nothing>()
    object Loading : ResultArticle<Nothing>()
}