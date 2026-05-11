package com.jpalomino502.vivebien.feature.home.data

import com.jpalomino502.vivebien.core.domain.model.DailyStats
import kotlinx.coroutines.flow.Flow

data class HomeDashboardData(
    val displayName: String = "",
    val currentDate: String = "",
    val planProgress: Float = 0f,
    val planProgressPercent: Int = 0,
    val nextAppointment: String = "Sin citas próximas",
    val dailyStats: DailyStats = DailyStats()
)

interface HomeRepository {
    fun getDashboardData(): Flow<HomeDashboardData>
}
