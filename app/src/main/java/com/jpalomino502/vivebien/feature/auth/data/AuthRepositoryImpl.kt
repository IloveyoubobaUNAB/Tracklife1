package com.jpalomino502.vivebien.feature.auth.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val isLoggedInKey = booleanPreferencesKey("is_logged_in")
    private val registeredUserKey = stringPreferencesKey("registered_user")
    private val registeredPassKey = stringPreferencesKey("registered_pass")
    private val displayNameKey = stringPreferencesKey("display_name")

    override suspend fun login(username: String, password: String): Result<Unit> {
        val prefs = context.authDataStore.data.first()
        val savedUser = prefs[registeredUserKey]
        val savedPass = prefs[registeredPassKey]
        val valid = if (savedUser != null && savedPass != null) {
            username == savedUser && password == savedPass
        } else {
            username == DEMO_USER && password == DEMO_PASSWORD
        }
        return if (valid) {
            context.authDataStore.edit { it[isLoggedInKey] = true }
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Credenciales incorrectas"))
        }
    }

    override suspend fun register(username: String, password: String, displayName: String): Result<Unit> {
        val prefs = context.authDataStore.data.first()
        if (prefs[registeredUserKey] == username) {
            return Result.failure(IllegalArgumentException("El usuario ya existe"))
        }
        context.authDataStore.edit {
            it[registeredUserKey] = username
            it[registeredPassKey] = password
            it[displayNameKey] = displayName
            it[isLoggedInKey] = true
        }
        return Result.success(Unit)
    }

    override suspend fun logout() {
        context.authDataStore.edit { it[isLoggedInKey] = false }
    }

    override suspend fun isLoggedIn(): Boolean =
        context.authDataStore.data.first()[isLoggedInKey] ?: false

    override suspend fun updateDisplayName(name: String) {
        context.authDataStore.edit { it[displayNameKey] = name }
    }

    override fun getDisplayName(): Flow<String> =
        context.authDataStore.data.map { it[displayNameKey] ?: "" }

    override fun getUsername(): Flow<String> =
        context.authDataStore.data.map { it[registeredUserKey] ?: "" }

    companion object {
        private const val DEMO_USER = "vivebien"
        private const val DEMO_PASSWORD = "1234"
    }
}
