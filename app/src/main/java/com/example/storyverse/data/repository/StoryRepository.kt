package com.example.storyverse.data.repository

import com.example.storyverse.data.remote.response.AddStoryResponse
import com.example.storyverse.data.remote.response.ListStoryResponse
import com.example.storyverse.data.remote.retofit.ApiService
import com.example.storyverse.domain.`interface`.StoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService) : StoryInterface {
    override suspend fun getStoryList(location : Int): ListStoryResponse {
        return withContext(Dispatchers.IO) {
            apiService.getStoryList(location)
        }
    }

    override suspend fun addStory(
        imageMultipart: MultipartBody.Part,
        description: RequestBody
    ): AddStoryResponse {
        return withContext(Dispatchers.IO) {
            apiService.addStory(imageMultipart, description)
        }
    }

    companion object{
        @Volatile
        private var instance : StoryRepository? = null

        fun getInstance(
            apiService:ApiService,
        ) : StoryRepository =
            instance ?: synchronized(this){
                instance ?: StoryRepository(apiService)
            }.also { instance=it }
    }
}