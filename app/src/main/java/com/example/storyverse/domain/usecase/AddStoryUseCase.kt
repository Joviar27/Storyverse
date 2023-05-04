package com.example.storyverse.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.storyverse.data.remote.response.AddStoryResponse
import com.example.storyverse.data.repository.StoryRepository
import com.example.storyverse.utils.ResultState
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryUseCase(private val storyRepository: StoryRepository) {
    fun addStoryWithoutLocation(
        imageMultipart : MultipartBody.Part,
        description : RequestBody
    ) : LiveData<ResultState<AddStoryResponse>> = liveData {
        emit(ResultState.Loading)
        try{
            val response = storyRepository.addStoryWithoutLocation(imageMultipart, description)
            val remoteResponse = MutableLiveData<ResultState<AddStoryResponse>>()
            remoteResponse.value = ResultState.Success(response)
            emitSource(remoteResponse)
        }
        catch (e : Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun addStoryWithLocation(
        imageMultipart : MultipartBody.Part,
        description : RequestBody,
        lat : RequestBody,
        lon : RequestBody
    ) : LiveData<ResultState<AddStoryResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = storyRepository.addStoryWithLocation(imageMultipart, description, lat, lon)
            val remoteResponse = MutableLiveData<ResultState<AddStoryResponse>>()
            remoteResponse.value = ResultState.Success(response)
            emitSource(remoteResponse)
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }
}