package com.jpalomino502.vivebien.feature.profile.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jpalomino502.vivebien.core.domain.model.User
import com.jpalomino502.vivebien.feature.auth.data.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_prefs")

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : ProfileRepository {

    private val emailKey = stringPreferencesKey("email")
    private val phoneKey = stringPreferencesKey("phone")
    private val birthDateKey = stringPreferencesKey("birth_date")
    private val notificationsKey = booleanPreferencesKey("notifications_enabled")
    private val darkModeKey = booleanPreferencesKey("dark_mode_enabled")

    override fun getUserProfile(): Flow<User> =
        combine(
            authRepository.getUsername(),
            authRepository.getDisplayName(),
            context.profileDataStore.data
        ) { username, displayName, prefs ->
            User(
                id = username,
                name = displayName.ifBlank { username },
                email = prefs[emailKey] ?: "",
                phone = prefs[phoneKey] ?: "",
                birthDate = prefs[birthDateKey] ?: ""
            )
        }

    override fun getNotificationsEnabled(): Flow<Boolean> =
        context.profileDataStore.data.map { it[notificationsKey] ?: true }

    override fun getDarkModeEnabled(): Flow<Boolean> =
        context.profileDataStore.data.map { it[darkModeKey] ?: false }

    override suspend fun updateProfile(user: User) {
        authRepository.updateDisplayName(user.name)
        context.profileDataStore.edit {
            it[emailKey] = user.email
            it[phoneKey] = user.phone
            it[birthDateKey] = user.birthDate
        }
    }

    override suspend fun saveNotificationsEnabled(enabled: Boolean) {
        context.profileDataStore.edit { it[notificationsKey] = enabled }
    }

    override suspend fun saveDarkModeEnabled(enabled: Boolean) {
        context.profileDataStore.edit { it[darkModeKey] = enabled }
    }
}
