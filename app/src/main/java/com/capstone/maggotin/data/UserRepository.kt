package com.capstone.maggotin.data

import com.capstone.maggotin.data.pref.UserModel
import com.capstone.maggotin.data.pref.UserPreference
import com.capstone.maggotin.data.remote.response.LoginRequest
import com.capstone.maggotin.data.remote.response.LoginResponse
import com.capstone.maggotin.data.remote.response.RegisterRequest
import com.capstone.maggotin.data.remote.response.RegisterResponse
import com.capstone.maggotin.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun loginUser(email: String, password: String): LoginResponse {
        val request = LoginRequest(email = email, password = password)
        return apiService.login(request)
    }

    suspend fun registerUser(request: RegisterRequest): RegisterResponse {
        return apiService.register(request)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}