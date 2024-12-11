package com.dicoding.picodiploma.loginwithanimation.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.maggotin.data.ArticleRepository
import com.capstone.maggotin.data.UserRepository
import com.capstone.maggotin.di.Injection
import com.capstone.maggotin.ui.home.HomeViewModel
import com.capstone.maggotin.ui.login.LoginViewModel
import com.capstone.maggotin.ui.main.MainViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository, articleRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val userRepository = Injection.provideUserRepository(context)
                    val articleRepository = Injection.provideArticleRepository(context)
                    INSTANCE = ViewModelFactory(userRepository, articleRepository)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}