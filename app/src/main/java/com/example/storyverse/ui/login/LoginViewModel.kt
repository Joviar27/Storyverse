package com.example.storyverse.ui.login

import androidx.lifecycle.ViewModel
import com.example.storyverse.data.local.UserPreference
import com.example.storyverse.domain.usecase.LoginUseCase

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    fun login(
        email : String,
        password : String
    )  = loginUseCase.execute(email, password)
}