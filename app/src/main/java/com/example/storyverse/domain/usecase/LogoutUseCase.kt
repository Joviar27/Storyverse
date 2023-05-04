package com.example.storyverse.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.storyverse.data.local.UserPreference
import com.example.storyverse.utils.ResultState

class LogoutUseCase(
    private val userPreference: UserPreference
) {
    fun execute() : LiveData<ResultState<String>> = liveData{
        emit(ResultState.Loading)
        try{
            userPreference.clearUserAuth()
            val response = MutableLiveData<ResultState<String>>()
            response.value = ResultState.Success("Logout Successful")
            emitSource(response)
        }
        catch (e : Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }
}