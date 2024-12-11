package com.capstone.maggotin.data.remote.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<ArticleItem>
)

data class ArticleItem(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("link_article")
    val linkArticle: String


)