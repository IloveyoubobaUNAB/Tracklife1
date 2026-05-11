package com.jpalomino502.vivebien.feature.profile.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.User
import com.jpalomino502.vivebien.feature.profile.data.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
    suspend operator fun invoke(user: User): Result<Unit> {
        if (user.name.isBlank()) return Result.failure(IllegalArgumentException("El nombre es obligatorio"))
        repository.updateProfile(user)
        return Result.success(Unit)
    }
}
