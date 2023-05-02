package com.example.storyverse.ui.splashscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyverse.data.di.Injection
import com.example.storyverse.domain.usecase.CheckAuthStateUseCase
import com.example.storyverse.domain.usecase.LoginUseCase

class SplashViewModelFactory (private val checkAuthStateUseCase: CheckAuthStateUseCase):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(checkAuthStateUseCase::class.java)) {
            return SplashViewModel(checkAuthStateUseCase) as T
        } else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(checkAuthStateUseCase) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: SplashViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): SplashViewModelFactory {
            return INSTANCE ?: synchronized(SplashViewModelFactory::class.java) {
                INSTANCE ?: SplashViewModelFactory(Injection.provideCheckAuthStateUseCase(context))
            }
        }
    }
}