package com.jpalomino502.vivebien.feature.activity.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.ActivityRecord
import com.jpalomino502.vivebien.feature.activity.data.ActivityRepository
import javax.inject.Inject

class AddActivityUseCase @Inject constructor(private val repository: ActivityRepository) {
    suspend operator fun invoke(
        title: String,
        durationMinutes: Int,
        distanceKm: Float?,
        scheduledTime: String?
    ): Result<Unit> {
        if (title.isBlank()) return Result.failure(IllegalArgumentException("El título es obligatorio"))
        if (durationMinutes <= 0) return Result.failure(IllegalArgumentException("La duración debe ser mayor a 0"))
        repository.addActivity(
            ActivityRecord(
                title = title.trim(),
                durationMinutes = durationMinutes,
                distanceKm = distanceKm?.takeIf { it > 0 },
                scheduledTime = scheduledTime?.takeIf { it.isNotBlank() }
            )
        )
        return Result.success(Unit)
    }
}
