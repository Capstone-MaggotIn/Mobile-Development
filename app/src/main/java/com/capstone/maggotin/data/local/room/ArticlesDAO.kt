package com.capstone.maggotin.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.capstone.maggotin.data.local.entity.ArticlesEntity

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articles ORDER BY date DESC")
    fun getArticles(): LiveData<List<ArticlesEntity>>

    @Query("SELECT * FROM articles WHERE is_bookmarked = 1")
    fun getBookmarkedArticles(): LiveData<List<ArticlesEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArticles(articles: List<ArticlesEntity>)

    @Update
    fun updateArticle(article: ArticlesEntity)

    @Query("DELETE FROM articles WHERE is_bookmarked = 0")
    fun deleteNonBookmarkedArticles()

    @Query("SELECT EXISTS(SELECT 1 FROM articles WHERE title = :title AND is_bookmarked = 1)")
    fun isArticleBookmarked(title: String): Boolean

    @Query("UPDATE articles SET is_bookmarked = :status WHERE id = :articleId")
    suspend fun updateBookmarkStatusById(articleId: Int, status: Boolean)
}