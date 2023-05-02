package com.example.storyverse.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.storyverse.data.local.UserPreference
import com.example.storyverse.data.remote.response.LoginResponse
import com.example.storyverse.domain.`interface`.AuthInterface
import com.example.storyverse.domain.entity.UserEntity
import com.example.storyverse.utils.ResultState

class LoginUseCase(
    private val authRepository: AuthInterface,
    private val userPreference: UserPreference
    ) {
    fun execute(
        email: String,
        password: String
    ): LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try{
            val responseLogin = authRepository.login(email,password)
            when(responseLogin.error){
                true -> {
                    emit(ResultState.Error(responseLogin.message))
                }
                false -> {
                    val remoteResponse = MutableLiveData<ResultState<LoginResponse>>()
                    remoteResponse.value = ResultState.Success(responseLogin)

                    val auth = UserEntity(
                        token = responseLogin.loginResult.token,
                        state = true
                    )
                    userPreference.setUserAuth(auth)

                    emitSource(remoteResponse)
                }
            }
        }
        catch (e : Exception){
            emit(ResultState.Error(e.message.toString()))
        }
    }
}