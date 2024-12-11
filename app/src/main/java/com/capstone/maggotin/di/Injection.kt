package com.capstone.maggotin.di

import android.content.Context
import com.capstone.maggotin.data.ArticleRepository
import com.capstone.maggotin.data.UserRepository
import com.capstone.maggotin.data.pref.UserPreference
import com.capstone.maggotin.data.pref.dataStore
import com.capstone.maggotin.data.local.room.ArticlesDatabase
import com.capstone.maggotin.data.remote.retrofit.ApiConfig
import com.capstone.maggotin.utils.AppExecutors

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getMaggotinApiService()
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideArticleRepository(context: Context): ArticleRepository {
        val apiService = ApiConfig.getMaggotinApiService()
        val database = ArticlesDatabase.getInstance(context)
        val dao = database.articlesDao()
        val appExecutors = AppExecutors()
        return ArticleRepository.getInstance(apiService, dao, appExecutors)
    }
}