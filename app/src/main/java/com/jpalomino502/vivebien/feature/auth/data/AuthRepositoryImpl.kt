package com.jpalomino502.vivebien.feature.auth.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val isLoggedInKey = booleanPreferencesKey("is_logged_in")

    override suspend fun login(username: String, password: String): Result<Unit> {
        return if (username == DEMO_USER && password == DEMO_PASSWORD) {
            context.authDataStore.edit { prefs -> prefs[isLoggedInKey] = true }
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Credenciales incorrectas"))
        }
    }

    override suspend fun logout() {
        context.authDataStore.edit { prefs -> prefs[isLoggedInKey] = false }
    }

    override suspend fun isLoggedIn(): Boolean {
        return context.authDataStore.data.first()[isLoggedInKey] ?: false
    }

    companion object {
        private const val DEMO_USER = "vivebien"
        private const val DEMO_PASSWORD = "1234"
    }
}
