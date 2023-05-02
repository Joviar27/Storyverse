package com.example.storyverse.domain.`interface`

import androidx.lifecycle.LiveData
import com.example.storyverse.data.remote.response.LoginResponse
import com.example.storyverse.data.remote.response.RegisterResponse
import com.example.storyverse.utils.ResultState

interface AuthInterface {
    suspend fun register(name : String, email : String, password : String) : RegisterResponse
    suspend fun login(email : String, password: String) : LoginResponse
}