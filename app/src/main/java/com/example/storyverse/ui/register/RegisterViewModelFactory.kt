package com.example.storyverse.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyverse.data.di.Injection
import com.example.storyverse.domain.usecase.RegisterUseCase

class RegisterViewModelFactory(private val registerUseCase: RegisterUseCase):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(registerUseCase) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(registerUseCase) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: RegisterViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): RegisterViewModelFactory {
            return INSTANCE ?: synchronized(RegisterViewModelFactory::class.java) {
                INSTANCE ?: RegisterViewModelFactory(Injection.provideRegisterUseCase(context))
            }
        }
    }
}