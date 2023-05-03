package com.example.storyverse.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.storyverse.data.repository.StoryRepository
import com.example.storyverse.domain.entity.StoryEntity
import com.example.storyverse.utils.ResultState

class GetStoryListUseCase(
    private val storyRepository: StoryRepository,
) {
    fun execute(location : Int) : LiveData<ResultState<List<StoryEntity>>> = liveData{
        emit(ResultState.Loading)
        try{
            val responseStory = storyRepository.getStoryList(location)
            val story = responseStory.listStory.map {
                StoryEntity(
                    id = it.id,
                    name = it.name,
                    photoUri = it.photoUrl,
                    description = it.description,
                    lat = it.lat,
                    lon = it.lon
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