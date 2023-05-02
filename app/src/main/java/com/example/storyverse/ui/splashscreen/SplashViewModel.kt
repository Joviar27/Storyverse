package com.example.storyverse.ui.splashscreen

import androidx.lifecycle.ViewModel
import com.example.storyverse.domain.usecase.CheckAuthStateUseCase
import com.example.storyverse.domain.usecase.LoginUseCase

class SplashViewModel(private val checkAuthStateUseCase: CheckAuthStateUseCase) : ViewModel() {
    fun checkAuthState() = checkAuthStateUseCase.execute()
}