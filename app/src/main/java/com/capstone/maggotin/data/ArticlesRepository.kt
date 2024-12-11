package com.capstone.maggotin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.capstone.maggotin.data.local.entity.ArticlesEntity
import com.capstone.maggotin.data.local.room.ArticlesDao
import com.capstone.maggotin.data.remote.response.ArticleResponse
import com.capstone.maggotin.data.remote.retrofit.ApiService
import com.capstone.maggotin.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleRepository(
    private val apiService: ApiService,
    private val articleDao: ArticlesDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<ResultArticle<List<ArticlesEntity>>>()

    fun getArticles(): LiveData<ResultArticle<List<ArticlesEntity>>> {
        result.value = ResultArticle.Loading

        val client = apiService.getArticles()
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                if (response.isSuccessful) {
                    val articles = response.body()?.data
                    val articleList = ArrayList<ArticlesEntity>()

                    appExecutors.diskIO.execute {
                        articles?.forEach { article ->
                            val isBookmarked = articleDao.isArticleBookmarked(article.title)
                            val articleEntity = ArticlesEntity(
                                article.id,
                                article.image,
                                article.title,
                                article.date,
                                article.description,
                                article.linkArticle,
                                isBookmarked
                            )
                            articleList.add(articleEntity)
                        }

                        // Delete old articles and insert new ones
                        articleDao.deleteNonBookmarkedArticles()
                        articleDao.insertArticles(articleList)
                    }

                    // Return success
                    result.value = ResultArticle.Success(articleList)
                } else {
                    result.value = ResultArticle.Error("Failed to fetch articles")
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                result.value = ResultArticle.Error(t.message.toString())
            }
        })

        // Fetch from local DB as a fallback
        val localData = articleDao.getArticles()
        result.addSource(localData) { newData: List<ArticlesEntity> ->
            result.value = ResultArticle.Success(newData)
        }

        return result
    }

    fun getBookmarkedArticles(): LiveData<List<ArticlesEntity>> {
        return articleDao.getBookmarkedArticles()
    }

    suspend fun updateBookmarkStatus(articleId: Int, status: Boolean) {
        articleDao.updateBookmarkStatusById(articleId, status)
    }

    fun setBookmarkedArticle(article: ArticlesEntity, bookmarkState: Boolean) {
        appExecutors.diskIO.execute {
            article.isBookmarked = bookmarkState
            articleDao.updateArticle(article)
        }
    }

    companion object {
        @Volatile
        private var instance: ArticleRepository? = null
        fun getInstance(
            apiService: ApiService,
            articleDao: ArticlesDao,
            appExecutors: AppExecutors
        ): ArticleRepository =
            instance ?: synchronized(this) {
                instance ?: ArticleRepository(apiService, articleDao, appExecutors)
            }.also { instance = it }
    }
}