package com.example.storyverse.domain.`interface`

import com.example.storyverse.data.remote.response.AddStoryResponse
import com.example.storyverse.data.remote.response.ListStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryInterface {
    suspend fun getStoryList() : ListStoryResponse
    suspend fun addStory(imageMultipart : MultipartBody.Part, description : RequestBody) : AddStoryResponse
}