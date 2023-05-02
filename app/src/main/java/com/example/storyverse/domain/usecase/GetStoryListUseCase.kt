package com.example.storyverse.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.storyverse.data.remote.response.ListStoryResponse
import com.example.storyverse.data.repository.StoryRepository
import com.example.storyverse.domain.entity.StoryEntity
import com.example.storyverse.domain.entity.UserEntity
import com.example.storyverse.utils.ResultState

class GetStoryListUseCase(
    private val storyRepository: StoryRepository,
) {
    fun execute() : LiveData<ResultState<List<StoryEntity>>> = liveData{
        emit(ResultState.Loading)
        try{
            val responseStory = storyRepository.getStoryList()
            val story = responseStory.listStory.map {
                StoryEntity(
                    id = it.id,
                    name = it.name,
                    photoUri = it.photoUrl,
                    description = it.description
                )
            }
            val remoteResponse = MutableLiveData<ResultState<List<StoryEntity>>>()
            remoteResponse.value = ResultState.Success(story)
            emitSource(remoteResponse)
        }
        catch (e : Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }
}