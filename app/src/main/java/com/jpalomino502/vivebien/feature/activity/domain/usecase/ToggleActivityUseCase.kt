package com.jpalomino502.vivebien.feature.activity.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.ActivityRecord
import com.jpalomino502.vivebien.feature.activity.data.ActivityRepository
import javax.inject.Inject

class ToggleActivityUseCase @Inject constructor(private val repository: ActivityRepository) {
    suspend operator fun invoke(activity: ActivityRecord) = repository.toggleCompleted(activity)
}
