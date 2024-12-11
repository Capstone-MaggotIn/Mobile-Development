package com.capstone.maggotin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.maggotin.data.ArticleRepository
import com.capstone.maggotin.data.ResultArticle
import com.capstone.maggotin.data.UserRepository
import com.capstone.maggotin.data.local.entity.ArticlesEntity
import com.capstone.maggotin.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository
) : ViewModel()  {
    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun getArticles(): LiveData<ResultArticle<List<ArticlesEntity>>> {
        return articleRepository.getArticles()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun setBookmarkedArticle(article: ArticlesEntity, bookmarkState: Boolean) {
        viewModelScope.launch {
            articleRepository.setBookmarkedArticle(article, bookmarkState)
        }
    }

    fun getBookmarkedArticles(): LiveData<List<ArticlesEntity>> {
        return articleRepository.getBookmarkedArticles()
    }

    fun updateBookmarkStatus(article: ArticlesEntity, bookmarkState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedStatus = !article.isBookmarked
            article.isBookmarked = updatedStatus
            articleRepository.updateBookmarkStatus(article.id, bookmarkState)
        }
    }
}