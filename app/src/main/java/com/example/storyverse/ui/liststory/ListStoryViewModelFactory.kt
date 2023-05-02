package com.example.storyverse.ui.liststory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyverse.data.di.Injection
import com.example.storyverse.domain.usecase.GetStoryListUseCase
import com.example.storyverse.domain.usecase.LogoutUseCase

class ListStoryViewModelFactory (
    private val getStoryListUseCase: GetStoryListUseCase,
    private val logoutUseCase: LogoutUseCase
    ):
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListStoryViewModel::class.java)) {
            return ListStoryViewModel(getStoryListUseCase, logoutUseCase) as T
        } else if (modelClass.isAssignableFrom(ListStoryViewModel::class.java)) {
            return ListStoryViewModel(getStoryListUseCase, logoutUseCase) as T
        }
        throw IllegalArgumentException("Unknown view model class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ListStoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ListStoryViewModelFactory {
            return INSTANCE ?: synchronized(ListStoryViewModelFactory::class.java) {
                INSTANCE ?: ListStoryViewModelFactory(Injection.provideGetListStoryUseCase(context), Injection.provideLogoutUseCase(context))
            }
        }
    }
}