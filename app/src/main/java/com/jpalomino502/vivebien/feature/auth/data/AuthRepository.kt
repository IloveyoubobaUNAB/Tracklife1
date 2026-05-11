package com.jpalomino502.vivebien.feature.auth.data

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun register(username: String, password: String, displayName: String): Result<Unit>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
    suspend fun updateDisplayName(name: String)
    fun getDisplayName(): Flow<String>
    fun getUsername(): Flow<String>
}
