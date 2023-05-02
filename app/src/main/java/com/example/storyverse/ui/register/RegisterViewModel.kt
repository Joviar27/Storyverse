package com.example.storyverse.ui.register

import androidx.lifecycle.ViewModel
import com.example.storyverse.domain.usecase.RegisterUseCase

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel(){
    fun register(
        name : String,
        email : String,
        password : String
    ) = registerUseCase.execute(name, email, password)
}