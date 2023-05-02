package com.example.storyverse.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.storyverse.data.local.UserPreference
import com.example.storyverse.domain.entity.UserEntity
import com.example.storyverse.utils.ResultState
import kotlinx.coroutines.flow.firstOrNull

class CheckAuthStateUseCase(private val userPreference: UserPreference) {
    fun execute(): LiveData<ResultState<UserEntity?>> = liveData {
        emit(ResultState.Loading)
        try {
            val user = userPreference.userAuthFlow.firstOrNull()
            if (user == null) {
                emit(ResultState.Error("User is null"))
            } else {
                val authState = MutableLiveData<ResultState<UserEntity?>>()
                authState.value = ResultState.Success(user)
                emitSource(authState)
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }
}