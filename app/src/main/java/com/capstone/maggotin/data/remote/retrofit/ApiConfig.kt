package com.capstone.maggotin.data.remote.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private fun createRetrofit(baseUrl: String): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getMaggotinApiService(): ApiService {
        return createRetrofit("https://backend-maggotin-138690736630.asia-southeast2.run.app/")
            .create(ApiService::class.java)
    }

    fun getModelApiService(): ApiService {
        return createRetrofit("https://backend-model-138690736630.asia-southeast2.run.app/")
            .create(ApiService::class.java)
    }
}