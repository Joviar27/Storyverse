package com.example.storyverse.ui.addstory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyverse.data.di.Injection
import com.example.storyverse.domain.usecase.AddStoryUseCase
import com.example.storyverse.ui.login.LoginViewModel

class AddStoryViewModelFactory (private val addStoryUseCase: AddStoryUseCase):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return AddStoryViewModel(addStoryUseCase) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(addStoryUseCase) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: AddStoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): AddStoryViewModelFactory {
            return INSTANCE ?: synchronized(AddStoryViewModelFactory::class.java) {
                INSTANCE ?: AddStoryViewModelFactory(Injection.provideAddStoryUseCase(context))
            }
        }
    }
}