package com.capstone.maggotin.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticlesEntity(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "link_article")
    val linkArticle: String,

    @ColumnInfo(name = "is_bookmarked")
    var isBookmarked: Boolean = false
)