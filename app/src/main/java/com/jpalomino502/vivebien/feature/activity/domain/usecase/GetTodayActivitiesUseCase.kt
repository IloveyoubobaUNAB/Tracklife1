package com.jpalomino502.vivebien.feature.activity.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.ActivityRecord
import com.jpalomino502.vivebien.feature.activity.data.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayActivitiesUseCase @Inject constructor(private val repository: ActivityRepository) {
    operator fun invoke(): Flow<List<ActivityRecord>> = repository.getTodayActivities()
}
