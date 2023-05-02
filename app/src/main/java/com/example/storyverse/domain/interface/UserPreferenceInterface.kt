package com.example.storyverse.domain.`interface`

import com.example.storyverse.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserPreferenceInterface {
    val userAuthFlow : Flow<UserEntity>
    suspend fun setUserAuth(userEntity: UserEntity)
    suspend fun clearUserAuth()
}