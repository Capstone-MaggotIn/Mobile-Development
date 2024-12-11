package com.capstone.maggotin.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.maggotin.data.UserRepository
import com.capstone.maggotin.data.pref.UserModel
import com.capstone.maggotin.data.remote.response.LoginResponse
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponse>?>()
    val loginResult: LiveData<Result<LoginResponse>?> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun clearLoginResult() {
        _loginResult.value = null
    }

    fun login(email: String, password: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.loginUser(email, password)
                val token = response.loginResult?.token
                if (token != null) {
                    repository.saveSession(
                        UserModel(
                            email = email,
                            token = token
                        )
                    )
                    _loginResult.postValue(Result.success(response))
                } else {
                    throw Exception("Login failed: Token is null")
                }
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let { JSONObject(it).getString("message") }
                } catch (jsonException: Exception) {
                    "Login failed"
                }
                _loginResult.postValue(
                    Result.success(LoginResponse(error = true, message = errorMessage))
                )
            } catch (e: Exception) {
                _loginResult.postValue(Result.failure(e))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
