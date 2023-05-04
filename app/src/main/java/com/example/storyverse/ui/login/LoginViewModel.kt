package com.example.storyverse.ui.login

import androidx.lifecycle.ViewModel
import com.example.storyverse.domain.usecase.CheckAuthStateUseCase
import com.example.storyverse.domain.usecase.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val checkAuthStateUseCase: CheckAuthStateUseCase
    )
    : ViewModel() {

    fun login(
        email : String,
        password : String
    )  = loginUseCase.execute(email, password)

    fun checkAuthState() = checkAuthStateUseCase.execute()
}