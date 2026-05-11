package com.jpalomino502.vivebien.feature.activity.data

import com.jpalomino502.vivebien.core.data.local.dao.ActivityDao
import com.jpalomino502.vivebien.core.data.local.entity.toDomain
import com.jpalomino502.vivebien.core.data.local.entity.toEntity
import com.jpalomino502.vivebien.core.domain.model.ActivityRecord
import com.jpalomino502.vivebien.core.domain.model.DailyStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRepositoryImpl @Inject constructor(
    private val dao: ActivityDao
) : ActivityRepository {

    private fun todayRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, 1)
        return start to cal.timeInMillis
    }

    override fun getTodayActivities(): Flow<List<ActivityRecord>> {
        val (start, end) = todayRange()
        return dao.getByDateRange(start, end).map { list -> list.map { it.toDomain() } }
    }

    override fun getDailyStats(): Flow<DailyStats> =
        getTodayActivities().map { activities ->
            val completed = activities.filter { it.completed }
            val totalDist = completed.sumOf { (it.distanceKm ?: 0f).toDouble() }.toFloat()
            val steps = (totalDist * 1300).toInt()
            val calories = completed.sumOf { it.durationMinutes * 5 }
            val goalPercent = if (activities.isEmpty()) 0 else completed.size * 100 / activities.size
            DailyStats(steps = steps, calories = calories, distanceKm = totalDist, goalPercent = goalPercent)
        }

    override suspend fun addActivity(activity: ActivityRecord) = dao.insert(activity.toEntity()).let {}

    override suspend fun toggleCompleted(activity: ActivityRecord) =
        dao.update(activity.copy(completed = !activity.completed).toEntity())

    override suspend fun deleteActivity(activity: ActivityRecord) = dao.delete(activity.toEntity())
}
