package com.example.storyverse.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.storyverse.data.remote.response.RegisterResponse
import com.example.storyverse.data.repository.AuthRepository
import com.example.storyverse.domain.`interface`.AuthInterface
import com.example.storyverse.utils.ResultState

class RegisterUseCase(private val authRepository: AuthRepository) {
    fun execute(
        name: String,
        email: String,
        password: String,
    ): LiveData<ResultState<RegisterResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val responseRegister = authRepository.register(name, email, password)
            when(responseRegister.error){
                true -> emit(ResultState.Error(responseRegister.message))
                false ->{
                    val remoteResponse = MutableLiveData<ResultState<RegisterResponse>>()
                    remoteResponse.value = ResultState.Success(responseRegister)
                    emitSource(remoteResponse)
                }
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }
}