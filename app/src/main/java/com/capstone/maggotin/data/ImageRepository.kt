package com.capstone.maggotin.data

import com.capstone.maggotin.data.remote.response.ClassifyResponse
import com.capstone.maggotin.data.remote.retrofit.ApiService
import okhttp3.MultipartBody

class ImageRepository(private val apiService: ApiService) {

    suspend fun uploadImage(file: MultipartBody.Part): ClassifyResponse {
        return apiService.uploadImage(file)
    }

    companion object {
        @Volatile
        private var instance: ImageRepository? = null

        fun getInstance(apiService: ApiService): ImageRepository =
            instance ?: synchronized(this) {
                instance ?: ImageRepository(apiService)
            }.also { instance = it }
    }
}
