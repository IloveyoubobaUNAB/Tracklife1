package com.jpalomino502.vivebien.feature.profile.domain.usecase

import com.jpalomino502.vivebien.feature.auth.data.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}
