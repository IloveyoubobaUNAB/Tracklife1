package com.jpalomino502.vivebien.feature.activity.data

import com.jpalomino502.vivebien.core.domain.model.ActivityRecord
import com.jpalomino502.vivebien.core.domain.model.DailyStats
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getTodayActivities(): Flow<List<ActivityRecord>>
    fun getDailyStats(): Flow<DailyStats>
    suspend fun addActivity(activity: ActivityRecord)
    suspend fun toggleCompleted(activity: ActivityRecord)
    suspend fun deleteActivity(activity: ActivityRecord)
}
