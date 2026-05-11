package com.jpalomino502.vivebien.feature.profile.domain.usecase

import com.jpalomino502.vivebien.feature.profile.data.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(private val repository: ProfileRepository) {
    fun notificationsEnabled(): Flow<Boolean> = repository.getNotificationsEnabled()
    fun darkModeEnabled(): Flow<Boolean> = repository.getDarkModeEnabled()
}

class SaveSettingsUseCase @Inject constructor(private val repository: ProfileRepository) {
    suspend fun saveNotifications(enabled: Boolean) = repository.saveNotificationsEnabled(enabled)
    suspend fun saveDarkMode(enabled: Boolean) = repository.saveDarkModeEnabled(enabled)
}
