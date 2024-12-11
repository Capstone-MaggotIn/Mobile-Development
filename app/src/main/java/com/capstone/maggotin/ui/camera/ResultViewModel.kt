package com.capstone.maggotin.ui.camera

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.capstone.maggotin.data.ImageRepository
import com.capstone.maggotin.data.remote.response.ClassifyResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ResultViewModel(private val imageRepository: ImageRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<ClassifyResponse>()
    val uploadResult: LiveData<ClassifyResponse> get() = _uploadResult

    fun uploadImage(file: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val result = imageRepository.uploadImage(file)
                _uploadResult.value = result
            } catch (e: Exception) {
                _uploadResult.value = ClassifyResponse(
                    message = "Error: ${e.message}",
                    status = "FAILED"
                )
            }
        }
    }
}

class ResultViewModelFactory(private val imageRepository: ImageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultViewModel(imageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
