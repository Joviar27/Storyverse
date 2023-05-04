package com.example.storyverse.data.repository

import com.example.storyverse.data.remote.response.LoginResponse
import com.example.storyverse.data.remote.response.RegisterResponse
import com.example.storyverse.data.remote.retofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val apiService: ApiService){
      suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return withContext(Dispatchers.IO) {
            apiService.register(name, email, password)
        }
    }

     suspend fun login(email: String, password: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            apiService.login(email, password)
        }
    }

    companion object{
        @Volatile
        private var instance : AuthRepository? = null

        fun getInstance(
            apiService:ApiService,
        ) : AuthRepository =
            instance ?: synchronized(this){
                instance ?: AuthRepository(apiService)
            }.also { instance=it }
    }
}