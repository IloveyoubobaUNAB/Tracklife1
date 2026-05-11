package com.jpalomino502.vivebien.feature.auth.domain.usecase

import com.jpalomino502.vivebien.feature.auth.data.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> {
        val trimmedUser = username.trim()
        val trimmedPass = password.trim()
        if (trimmedUser.isBlank() || trimmedPass.isBlank()) {
            return Result.failure(IllegalArgumentException("Usuario y contraseña son obligatorios"))
        }
        return repository.login(trimmedUser, trimmedPass)
    }
}
