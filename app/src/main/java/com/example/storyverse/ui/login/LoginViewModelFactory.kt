package com.example.storyverse.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyverse.data.di.Injection
import com.example.storyverse.domain.usecase.LoginUseCase

class LoginViewModelFactory (private val loginUseCase: LoginUseCase):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginUseCase) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginUseCase) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): LoginViewModelFactory {
            return INSTANCE ?: synchronized(LoginViewModelFactory::class.java) {
                INSTANCE ?: LoginViewModelFactory(Injection.provideLoginUseCase(context))
            }
        }
    }
}