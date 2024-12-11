package com.capstone.maggotin.data.remote.retrofit

import com.capstone.maggotin.data.remote.response.ArticleResponse
import com.capstone.maggotin.data.remote.response.ClassifyResponse
import com.capstone.maggotin.data.remote.response.LoginRequest
import com.capstone.maggotin.data.remote.response.LoginResponse
import com.capstone.maggotin.data.remote.response.RegisterRequest
import com.capstone.maggotin.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<ArticleResponse>

    @Headers("Content-Type: application/json")
    @POST("users/register")
    suspend fun register(
        @Body requestBody: RegisterRequest
    ): RegisterResponse

    @Headers("Content-Type: application/json")
    @POST("users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): ClassifyResponse
}