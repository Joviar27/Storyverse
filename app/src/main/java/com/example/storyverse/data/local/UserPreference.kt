package com.example.storyverse.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.storyverse.domain.`interface`.UserPreferenceInterface
import com.example.storyverse.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(private val dataStore: DataStore<Preferences>){

    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    private val AUTH_STATE_KEY = booleanPreferencesKey("auth_state")

     val userAuthFlow: Flow<UserEntity> = dataStore.data
        .map { preference ->
            UserEntity(
                preference[AUTH_STATE_KEY] ?: false,
                preference[AUTH_TOKEN_KEY] ?: ""
            )
        }

     suspend fun setUserAuth(userEntity: UserEntity) {
        dataStore.edit {
            it[AUTH_STATE_KEY] = userEntity.state
            it[AUTH_TOKEN_KEY] = userEntity.token
        }
    }

     suspend fun clearUserAuth() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object{
        @Volatile
        private var INSTANCE : UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference{
            //synchronized used so all thread are sync and only one thread that can run this
            return INSTANCE ?: synchronized(this){
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}