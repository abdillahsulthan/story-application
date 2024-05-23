package com.dicoding.storyapp.ui.uploadstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.remote.response.CreateStory
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadStoryViewModel(private val token: String): ViewModel() {

    private val _createStoryResponse = MutableLiveData<CreateStory>()
    val createStoryResponse: LiveData<CreateStory> = _createStoryResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(description: RequestBody, image: MultipartBody.Part, lat: RequestBody?, lon: RequestBody?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadStory(token = "Bearer $token", description = description, file = image, lat = lat, lon = lon)
        client.enqueue(object : Callback<CreateStory> {
            override fun onResponse(
                call: Call<CreateStory>,
                response: Response<CreateStory>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _createStoryResponse.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CreateStory>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "DetailViewModel"
    }

}