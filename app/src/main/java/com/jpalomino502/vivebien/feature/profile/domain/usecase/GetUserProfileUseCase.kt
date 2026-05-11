package com.jpalomino502.vivebien.feature.profile.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.User
import com.jpalomino502.vivebien.feature.profile.data.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
    operator fun invoke(): Flow<User> = repository.getUserProfile()
}
