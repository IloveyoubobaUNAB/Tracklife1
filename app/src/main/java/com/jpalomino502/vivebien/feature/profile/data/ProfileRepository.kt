package com.jpalomino502.vivebien.feature.profile.data

import com.jpalomino502.vivebien.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUserProfile(): Flow<User>
    fun getNotificationsEnabled(): Flow<Boolean>
    fun getDarkModeEnabled(): Flow<Boolean>
    suspend fun updateProfile(user: User)
    suspend fun saveNotificationsEnabled(enabled: Boolean)
    suspend fun saveDarkModeEnabled(enabled: Boolean)
}
