package com.example.storyverse.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.example.storyverse.data.StoryRemoteMediator
import com.example.storyverse.data.local.StoryDatabase
import com.example.storyverse.data.remote.response.AddStoryResponse
import com.example.storyverse.data.remote.response.ListStoryResponse
import com.example.storyverse.data.remote.retofit.ApiService
import com.example.storyverse.domain.`interface`.StoryInterface
import com.example.storyverse.domain.entity.StoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
    ) {
     suspend fun getStoryList(location : Int): ListStoryResponse {
        return withContext(Dispatchers.IO) {
            apiService.getStoryList(location,1,20)
        }
    }

    fun getPagingConfig() : PagingConfig{
        return PagingConfig(
            pageSize = 5,
            initialLoadSize = 20,
            enablePlaceholders = true,
        )
    }

    fun getRemoteMediator() : StoryRemoteMediator{
        return StoryRemoteMediator(storyDatabase, apiService)
    }

    fun getPagingSourceFactory() : PagingSource<Int, StoryEntity>{
        return storyDatabase.storyDao().getAllStory()
    }

    suspend fun addStory(
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
            storyDatabase: StoryDatabase
        ) : StoryRepository =
            instance ?: synchronized(this){
                instance ?: StoryRepository(apiService,storyDatabase)
            }.also { instance=it }
    }
}