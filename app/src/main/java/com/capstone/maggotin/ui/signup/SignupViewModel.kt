package com.capstone.maggotin.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.maggotin.data.UserRepository
import com.capstone.maggotin.data.remote.response.RegisterRequest
import com.capstone.maggotin.data.remote.response.RegisterResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registrationResponse = MutableLiveData<RegisterResponse>()
    val registrationResponse: LiveData<RegisterResponse> = _registrationResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        val request = RegisterRequest(name, email, password, confirmPassword)

        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = userRepository.registerUser(request)
                _registrationResponse.postValue(response)
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let { JSONObject(it).getString("message") }
                } catch (jsonException: Exception) {
                    "Registration failed"
                }
                Log.e("SignupViewModel", "Error: $errorMessage")
                _registrationResponse.postValue(RegisterResponse(error = true, message = errorMessage))
            } catch (e: Exception) {
                e.printStackTrace()
                _registrationResponse.postValue(RegisterResponse(error = true, message = "Registration failed"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}

