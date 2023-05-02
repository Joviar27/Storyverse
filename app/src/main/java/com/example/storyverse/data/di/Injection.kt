package com.example.storyverse.data.di

import android.content.Context
import com.example.storyverse.data.remote.retofit.ApiConfig
import com.example.storyverse.data.repository.AuthRepository
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyverse.data.local.UserPreference
import com.example.storyverse.data.repository.StoryRepository
import com.example.storyverse.domain.usecase.*

object Injection {

    private val Context.dataStore by preferencesDataStore(name = "user_preferences")

    private fun provideAuthRepository(context: Context): AuthRepository {
        val dataStore = context.dataStore
        val apiService = ApiConfig.getApiService(dataStore)
        return AuthRepository.getInstance(apiService)
    }

    private fun provideStoryRepository(context: Context) : StoryRepository{
        val dataStore = context.dataStore
        val apiService = ApiConfig.getApiService(dataStore)
        return StoryRepository.getInstance(apiService)
    }

    private fun provideUserPreference(context: Context) : UserPreference{
        val dataStore = context.dataStore
        return UserPreference(dataStore)
    }

    fun provideRegisterUseCase(context: Context): RegisterUseCase {
        val authRepository = provideAuthRepository(context)
        return RegisterUseCase(authRepository)
    }

    fun provideLoginUseCase(context: Context) : LoginUseCase{
        val userPreference = provideUserPreference(context)
        val authRepository = provideAuthRepository(context)
        return LoginUseCase(authRepository,userPreference)
    }

    fun provideLogoutUseCase(context: Context) : LogoutUseCase{
        val userPreference = provideUserPreference(context)
        return LogoutUseCase(userPreference)
    }

    fun provideCheckAuthStateUseCase(context: Context) : CheckAuthStateUseCase{
        val userPreference = provideUserPreference(context)
        return CheckAuthStateUseCase(userPreference)
    }

    fun provideGetListStoryUseCase(context: Context) : GetStoryListUseCase{
        val storyRepository = provideStoryRepository(context)
        return GetStoryListUseCase(storyRepository)
    }

    fun provideAddStoryUseCase(context: Context) : AddStoryUseCase {
        val storyRepository = provideStoryRepository(context)
        return AddStoryUseCase(storyRepository)
    }
}