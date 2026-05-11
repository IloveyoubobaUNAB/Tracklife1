package com.jpalomino502.vivebien.feature.auth.domain.usecase

import com.jpalomino502.vivebien.feature.auth.data.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        confirmPassword: String
    ): Result<Unit> {
        val trimmedUser = username.trim()
        val trimmedPass = password.trim()

        if (trimmedUser.isBlank() || trimmedPass.isBlank() || confirmPassword.isBlank()) {
            return Result.failure(IllegalArgumentException("Todos los campos son obligatorios"))
        }
        if (trimmedUser.length < 3) {
            return Result.failure(IllegalArgumentException("El usuario debe tener al menos 3 caracteres"))
        }
        if (trimmedPass.length < 4) {
            return Result.failure(IllegalArgumentException("La contraseña debe tener al menos 4 caracteres"))
        }
        if (trimmedPass != confirmPassword.trim()) {
            return Result.failure(IllegalArgumentException("Las contraseñas no coinciden"))
        }
        return repository.register(trimmedUser, trimmedPass)
    }
}
